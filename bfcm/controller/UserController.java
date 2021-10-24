package com.seanco.bfcm.controller;


import com.seanco.bfcm.pojo.User;
import com.seanco.bfcm.vo.ResponseInfo;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author seanco
 * @since 2021-08-25
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("info")
    @ResponseBody
    public ResponseInfo info(User user) {
        return ResponseInfo.success(user);
    }

}
