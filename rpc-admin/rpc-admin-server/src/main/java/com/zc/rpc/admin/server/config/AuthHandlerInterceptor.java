package com.zc.rpc.admin.server.config;

import com.zc.rpc.admin.bean.exception.TokenAuthEmptyException;
import com.zc.rpc.admin.bean.exception.TokenAuthExpiredException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-10
 */
@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    @Autowired
    private TokenUtil tokenUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        String token = request.getHeader("token");
        if (token==null || token.trim().isEmpty()){
            throw new TokenAuthEmptyException();
        }
        Map<String, String> map = tokenUtil.parseToken(token);
        String userName = map.get("userName");
        long timeStamp = Long.parseLong(map.get("timeStamp"));
        if (System.currentTimeMillis()-timeStamp<1000*60*60*5){
            response.setHeader("token", tokenUtil.getToken(userName));
        }else {
            throw new TokenAuthExpiredException("用户："+userName+"的token过期，请重新登录");
        }
        return true;
    }
}
