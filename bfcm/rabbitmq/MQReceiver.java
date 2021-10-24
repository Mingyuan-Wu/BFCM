package com.seanco.bfcm.rabbitmq;

import com.seanco.bfcm.pojo.CountdownOrder;
import com.seanco.bfcm.pojo.User;
import com.seanco.bfcm.service.IOrderService;
import com.seanco.bfcm.service.IProductService;
import com.seanco.bfcm.utils.JsonUtil;
import com.seanco.bfcm.vo.CountdownOrderVo;
import com.seanco.bfcm.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {

    @Autowired
    private IProductService productService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private IOrderService orderService;

    /**
     * Perform order transactions.
     * @param msg
     */
    @RabbitListener(queues = "countdownOrderQueue")
    public void receive(String msg) throws InterruptedException {
        Thread.sleep(20*1000);
        log.info("received message: " + msg);
        CountdownOrderVo countdownOrderVo = JsonUtil.jsonStr2Object(msg, CountdownOrderVo.class);
        Long productId = countdownOrderVo.getProductId();
        User user = countdownOrderVo.getUser();
        ProductVo productVo = productService.findProductVoById(productId);
        if (productVo.getStockCount() <= 0) {
            return;
        }
        // check if duplicate ordered for that user
        CountdownOrder countdownOrder =
                (CountdownOrder) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + productId);
        if (countdownOrder != null) {
            // double ordered
            return;
        }
        orderService.submitOrder(user, productVo);
    }
}
