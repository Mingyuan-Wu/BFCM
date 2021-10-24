package com.seanco.bfcm.service;

import com.seanco.bfcm.pojo.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seanco.bfcm.pojo.User;
import com.seanco.bfcm.vo.OrderDetailVo;
import com.seanco.bfcm.vo.ProductVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author seanco
 * @since 2021-08-30
 */
public interface IOrderService extends IService<Order> {

    Order submitOrder(User user, ProductVo productVo);

    OrderDetailVo getOrderDetail(Long orderId);
}
