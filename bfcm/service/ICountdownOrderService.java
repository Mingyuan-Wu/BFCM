package com.seanco.bfcm.service;

import com.seanco.bfcm.pojo.CountdownOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seanco.bfcm.pojo.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author seanco
 * @since 2021-08-30
 */
public interface ICountdownOrderService extends IService<CountdownOrder> {

    Long getResult(User user, Long productId);
}
