package com.seanco.bfcm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.seanco.bfcm.pojo.CountdownOrder;
import com.seanco.bfcm.mapper.CountdownOrderMapper;
import com.seanco.bfcm.pojo.User;
import com.seanco.bfcm.service.ICountdownOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author seanco
 * @since 2021-08-30
 */
@Service
public class CountdownOrderServiceImpl extends ServiceImpl<CountdownOrderMapper, CountdownOrder> implements ICountdownOrderService {

    @Autowired
    private CountdownOrderMapper countdownOrderMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    // -1: failure
    //  0: to be processed
    // xx: order id
    @Override
    public Long getResult(User user, Long productId) {
        CountdownOrder countdownOrder = countdownOrderMapper.selectOne(
                new QueryWrapper<CountdownOrder>()
                        .eq("user_id", user.getId())
                        .eq("product_id", productId));
        if (countdownOrder != null) {
            return countdownOrder.getOrderId();
        } else if (redisTemplate.hasKey("stockEmpty:" + productId)) {
            return -1L;
        } else {
            return 0L;
        }
    }
}
