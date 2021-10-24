package com.seanco.bfcm.service;

import com.seanco.bfcm.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.seanco.bfcm.vo.LoginVo;
import com.seanco.bfcm.vo.ResponseInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author seanco
 * @since 2021-08-25
 */
public interface IUserService extends IService<User> {

    ResponseInfo doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(HttpServletRequest request, HttpServletResponse response, String userTicket);

    ResponseInfo updatePassword(HttpServletRequest request, HttpServletResponse response, String userTicket, String password);
}
