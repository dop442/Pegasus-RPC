package com.zc.rpc.admin.server.configure.controller;

import com.zc.rpc.admin.bean.POJO.ConsumerInfo;
import com.zc.rpc.admin.server.configure.service.ConfigureManageService;
import com.zc.rpc.admin.utils.constants.HttpCode;
import com.zc.rpc.admin.utils.resp.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-19
 */
@RestController
@RequestMapping("/configure")
public class ConfigureManageController {

    @Autowired
    private ConfigureManageService configureManageService;

    @PostMapping("/add")
    public Result<String> addConfigure(
            @RequestParam("configureName") String configureName,
            @RequestParam("consumerName") String consumerName,
            @RequestParam("serviceName") String serviceName,
            @RequestParam("version") String version,
            @RequestParam("group") String group,
            @RequestParam("configure") String configure
    ){
        configureManageService.addConfigure(configureName, consumerName, serviceName,
                version, group, configure);
        return new Result<>(HttpCode.SUCCESS, "创建成功", configureName);
    }


    @PostMapping("/remove")
    public Result<String> removeConfigure(@RequestParam("configureName") String configureName){
        configureManageService.deleteConfigure(configureName);
        return new Result<>(HttpCode.SUCCESS, "删除成功", configureName);
    }


    @PostMapping("/get")
    public Result<Map<String, String>> getConfigure(@RequestParam("configureName") String configureName){
        Map<String, String> map = configureManageService.getConfigure(configureName);
        return new Result<>(HttpCode.SUCCESS, "获取成功", map);
    }

    @GetMapping("/consumer")
    public Result<List<ConsumerInfo>> getAllConsumerInfo(){
        List<ConsumerInfo> consumerInfoList = configureManageService.getAllConsumerInfo();
        return new Result<>(HttpCode.SUCCESS, "获取成功", consumerInfoList);
    }

    @PostMapping("/edit")
    public Result<String> editConfigure(String configureName, String configure){
        configureManageService.updateConfigure(configureName, configure);
        return new Result<>(HttpCode.SUCCESS, "修改成功", configureName);
    }

    @GetMapping("/getAllConfigureName")
    public Result<List<String>> getAllConfigureName(){
        List<String> configureNameList=configureManageService.getAllConfigureName();
        return new Result<>(HttpCode.SUCCESS, "获取成功", configureNameList);
    }




}
