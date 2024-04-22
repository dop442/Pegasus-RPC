package com.zc.rpc.admin.server.login.controller;

import com.zc.rpc.admin.server.login.service.LoginService;
import com.zc.rpc.admin.utils.constants.HttpCode;
import com.zc.rpc.admin.utils.resp.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-05
 */
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping(value = "/login")
    public Result<String> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ){
        String token = loginService.login(username, password);
        if (token!=null){
            return new Result<>(HttpCode.SUCCESS, "登录成功", token);
        }
        return new Result<>(HttpCode.FAILURE, "登录失败", username);
    }

    @GetMapping("/")
    public String hello(){
        return "你好";
    }
}
