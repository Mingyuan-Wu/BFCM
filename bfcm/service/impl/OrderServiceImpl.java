package com.seanco.bfcm.service.impl;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.seanco.bfcm.exception.GlobalException;
import com.seanco.bfcm.mapper.CountdownOrderMapper;
import com.seanco.bfcm.pojo.CountdownOrder;
import com.seanco.bfcm.pojo.CountdownProduct;
import com.seanco.bfcm.pojo.Order;
import com.seanco.bfcm.mapper.OrderMapper;
import com.seanco.bfcm.pojo.User;
import com.seanco.bfcm.service.ICountdownProductService;
import com.seanco.bfcm.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seanco.bfcm.service.IProductService;
import com.seanco.bfcm.vo.OrderDetailVo;
import com.seanco.bfcm.vo.ProductVo;
import com.seanco.bfcm.vo.ResponseInfoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author seanco
 * @since 2021-08-30
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private ICountdownProductService countdownProductService;

    @Autowired
    private IProductService productService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CountdownOrderMapper countdownOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    @Transactional
    public Order submitOrder(User user, ProductVo productVo) {
        boolean result = countdownProductService.update(
                new UpdateWrapper<CountdownProduct>()
                        .setSql("stock_count = stock_count - 1")
                        .eq("product_id", productVo.getId())
                        .gt("stock_count", 0));
        if (!result) {
            // If update failed, means no stock left
            ValueOperations valueOperations = redisTemplate.opsForValue();
            valueOperations.set("stockEmpty:" + productVo.getId(), "1");
            throw new GlobalException(ResponseInfoEnum.NO_STOCK);
        }
        // generate order
        Order order = new Order();
        order.setUserId(user.getId());
        order.setProductId(productVo.getId());
        order.setDeliveryAddrId(0L);
        order.setProductName(productVo.getProductName());
        order.setProductCount(1);
        order.setProductPrice(productVo.getProductPrice());
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);

        // generate countdown order
        CountdownOrder countdownOrder = new CountdownOrder();
        countdownOrder.setUserId(user.getId());
        countdownOrder.setOrderId(order.getId());
        countdownOrder.setProductId(productVo.getId());
        countdownOrderMapper.insert(countdownOrder);

        redisTemplate.opsForValue().set("order:" + user.getId() + ":" + productVo.getId(), countdownOrder);
        return order;
    }

    @Override
    public OrderDetailVo getOrderDetail(Long orderId) {
        if (orderId == null) {
            throw new GlobalException(ResponseInfoEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        ProductVo productVo = productService.findProductVoById(order.getProductId());
        OrderDetailVo orderDetailVo = new OrderDetailVo();
        orderDetailVo.setOrder(order);
        orderDetailVo.setProductVo(productVo);
        return orderDetailVo;
    }
}
