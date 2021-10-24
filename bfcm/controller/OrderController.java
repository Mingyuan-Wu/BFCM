package com.seanco.bfcm.controller;


import com.seanco.bfcm.pojo.User;
import com.seanco.bfcm.service.IOrderService;
import com.seanco.bfcm.vo.OrderDetailVo;
import com.seanco.bfcm.vo.ResponseInfo;
import com.seanco.bfcm.vo.ResponseInfoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author seanco
 * @since 2021-08-30
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @RequestMapping("detail")
    @ResponseBody
    public ResponseInfo getOrderDetail(User user, Long orderId) {
        if (user == null) {
            return ResponseInfo.error(ResponseInfoEnum.USER_NOT_FOUND);
        }
        OrderDetailVo detail = orderService.getOrderDetail(orderId);
        return ResponseInfo.success(detail);
    }
}
