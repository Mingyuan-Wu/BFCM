package com.seanco.bfcm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seanco.bfcm.pojo.CountdownOrder;
import com.seanco.bfcm.pojo.Order;
import com.seanco.bfcm.pojo.User;
import com.seanco.bfcm.rabbitmq.MQSender;
import com.seanco.bfcm.service.ICountdownOrderService;
import com.seanco.bfcm.service.IOrderService;
import com.seanco.bfcm.service.IProductService;
import com.seanco.bfcm.utils.JsonUtil;
import com.seanco.bfcm.vo.CountdownOrderVo;
import com.seanco.bfcm.vo.ProductVo;
import com.seanco.bfcm.vo.ResponseInfo;
import com.seanco.bfcm.vo.ResponseInfoEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("countdown")
public class CountdownController implements InitializingBean {

    @Autowired
    private IProductService productService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICountdownOrderService countdownOrderService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisScript<Long> redisscript;

    @Autowired
    private MQSender mqSender;

    // <productId, isEmptyStock>
    private final Map<Long, Boolean> productMap = new HashMap<>();

    @PostMapping("submitOrder")
    @ResponseBody
    public ResponseInfo submitOrder(Model model, User user, Long productId) {
        if (user == null) {
            return ResponseInfo.error(ResponseInfoEnum.USER_NOT_FOUND);
        }
        ValueOperations valueOperations = redisTemplate.opsForValue();
        // check if duplicate ordered for that user
        CountdownOrder countdownOrder =
                (CountdownOrder) valueOperations.get("order:" + user.getId() + ":" + productId);
        if (countdownOrder != null) {
            // double ordered
            return ResponseInfo.error(ResponseInfoEnum.DUPLICATE_ORDER);
        }
        // Check if we have stock in memory, to reduce Redis traffic after sold out
        if (productMap.get(productId)) {
            return ResponseInfo.error(ResponseInfoEnum.NO_STOCK);
        }
        // atomic decrease stock count
//        Long stockCount = valueOperations.decrement("countdownProduct:" + productId);
        Long stockCount = (Long) redisTemplate.execute(redisscript, Collections.singletonList("countdownProduct:" + productId),
                Collections.EMPTY_LIST);
        if (stockCount < 0) {
            productMap.put(productId, true);
//            valueOperations.increment("countdownProduct:" + productId);
            return ResponseInfo.error(ResponseInfoEnum.NO_STOCK);
        }
        CountdownOrderVo countdownOrderVo = new CountdownOrderVo(user, productId);
        mqSender.sendCountdownOrderVo(JsonUtil.object2JsonStr(countdownOrderVo));
        return ResponseInfo.success(0);
    }

    // Will run during application start
    @Override
    public void afterPropertiesSet() throws Exception {
        List<ProductVo> productVoList = productService.findProductVo();
        if (CollectionUtils.isEmpty(productVoList)) {
            return;
        }
        productVoList.forEach(productVo -> {
            // preload all products in Redis
            redisTemplate.opsForValue().set("countdownProduct:" + productVo.getId(), productVo.getStockCount());
            // preload stock products in memory
            productMap.put(productVo.getId(), false);
        });
    }

    @RequestMapping("submitOrder2")
    public String submitOrder2(Model model, User user, Long productId) {
        if (user == null) {
            return "login";
        }
        model.addAttribute("user", user);
        ProductVo productVo = productService.findProductVoById(productId);
        if (productVo.getStockCount() <= 0) {
            model.addAttribute("errs", ResponseInfoEnum.NO_STOCK.getMsg());
            return "submitOrderFail";
        }
        // check if duplicate ordered
        CountdownOrder countdownOrder =
                countdownOrderService.getOne(new QueryWrapper<CountdownOrder>().eq("user_id", user.getId()).eq("product_id", productId));
        if (countdownOrder != null) {
            // double ordered
            model.addAttribute("errs", ResponseInfoEnum.DUPLICATE_ORDER.getMsg());
            return "submitOrderFail";
        }
        Order order = orderService.submitOrder(user, productVo);
        model.addAttribute("order", order);
        model.addAttribute("product", productVo);
        return "orderDetail";
    }

    /**
     * Try fetch order status in queue.
     * @param user
     * @param productId orderId: success, -1: failed, 0: in progress
     * @return
     */
    @RequestMapping("result")
    @ResponseBody
    public ResponseInfo gerResult(User user, Long productId) {
        if (user == null) {
            return ResponseInfo.error(ResponseInfoEnum.USER_NOT_FOUND);
        }
        Long orderId = countdownOrderService.getResult(user, productId);
        return ResponseInfo.success(orderId);
    }
}
