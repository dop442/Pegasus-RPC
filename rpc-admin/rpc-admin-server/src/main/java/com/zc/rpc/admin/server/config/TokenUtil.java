package com.zc.rpc.admin.server.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-11
 */
@Component
public class TokenUtil {

    @Value("${token.privateKey}")
    private String privateKey;


    public String getToken(String userName){
        return JWT.create()
                .withClaim("userName", userName)
                .withClaim("timeStamp", System.currentTimeMillis())
                .sign(Algorithm.HMAC256(privateKey));
    }

    public Map<String, String> parseToken(String token){
        Map<String, String> map = new HashMap<>();
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(privateKey)).build().verify(token);
        Claim userName = decodedJWT.getClaim("userName");
        Claim timeStamp = decodedJWT.getClaim("timeStamp");
        map.put("userName", userName.asString());
        map.put("timeStamp", timeStamp.asLong().toString());
        return map;
    }


}
