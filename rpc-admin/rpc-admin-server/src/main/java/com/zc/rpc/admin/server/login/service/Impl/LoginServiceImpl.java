package com.zc.rpc.admin.server.login.service.Impl;

import com.zc.rpc.admin.server.config.TokenUtil;
import com.zc.rpc.admin.server.login.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-05
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String login(String username, String password) {
        String passwordInRedis = (String) redisTemplate.opsForValue().get("user:" + username);
        if(password.equals(passwordInRedis)){
            return tokenUtil.getToken(username);
        }
        return null;
    }
}
