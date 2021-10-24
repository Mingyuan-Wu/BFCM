package com.seanco.bfcm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.seanco.bfcm.exception.GlobalException;
import com.seanco.bfcm.mapper.UserMapper;
import com.seanco.bfcm.pojo.User;
import com.seanco.bfcm.service.IUserService;
import com.seanco.bfcm.utils.CookieUtil;
import com.seanco.bfcm.utils.MD5Util;
import com.seanco.bfcm.utils.UUIDUtil;
import com.seanco.bfcm.vo.LoginVo;
import com.seanco.bfcm.vo.ResponseInfo;
import com.seanco.bfcm.vo.ResponseInfoEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author seanco
 * @since 2021-08-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public ResponseInfo doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();

        User user = userMapper.selectById(mobile);
        // user not exist
        if (user == null) {
            throw new GlobalException(ResponseInfoEnum.LOGIN_ERROR);
        }
        // password not correct
        if (!MD5Util.formPasswordToDBPassword(password, user.getSalt()).equals(user.getPassword())) {
            throw new GlobalException(ResponseInfoEnum.LOGIN_ERROR);
        }

        // set sessions & cookies
        String ticket = UUIDUtil.uuid();
//        request.getSession().setAttribute(ticket, user);

        // save user info to redis
        redisTemplate.opsForValue().set("user:" + ticket, user);

        CookieUtil.setCookie(request, response, "userTicket", ticket);

        return ResponseInfo.success(ticket);
    }

    @Override
    public User getUserByCookie(HttpServletRequest request, HttpServletResponse response, String userTicket) {
        User user = (User) redisTemplate.opsForValue().get("user:" + userTicket);
        if (user != null) {
            CookieUtil.setCookie(request, response, "userTicket", userTicket);
        }
        return user;
    }

    @Override
    public ResponseInfo updatePassword(HttpServletRequest request, HttpServletResponse response, String userTicket, String password) {
        User user = getUserByCookie(request, response, userTicket);
        if (user == null) {
            throw new GlobalException(ResponseInfoEnum.USER_NOT_FOUND);
        }
        user.setPassword(MD5Util.formPasswordToDBPassword(password, user.getSalt()));
        int result = userMapper.updateById(user);
        if (result == 1) {
            redisTemplate.delete("user:" + userTicket);
            return ResponseInfo.success();
        }
        return ResponseInfo.error(ResponseInfoEnum.PASSWORD_UPDATE_FAILED);
    }
}
