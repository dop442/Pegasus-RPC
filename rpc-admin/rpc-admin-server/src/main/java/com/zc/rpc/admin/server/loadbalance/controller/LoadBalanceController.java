package com.zc.rpc.admin.server.loadbalance.controller;

import com.zc.rpc.admin.bean.DTO.LoadBalanceInfo;
import com.zc.rpc.admin.bean.DTO.LoadBalanceServiceInfoDTO;
import com.zc.rpc.admin.server.loadbalance.service.LoadBalanceService;
import com.zc.rpc.admin.utils.constants.HttpCode;
import com.zc.rpc.admin.utils.resp.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-17
 */
@RestController
@RequestMapping("/loadbalance")
public class LoadBalanceController {

    @Autowired
    private LoadBalanceService loadBalanceService;


//    @PostMapping("/consumer")
//    public Result<Map<String,String>> getConsumer(
//            @RequestParam("consumerName")String applicationName
//    ){
//        Map<String, String> consumer = loadBalanceService.getConsumer(applicationName);
//        return new Result<>(HttpCode.SUCCESS, "查询消费者成功", consumer);
//    }


    @PostMapping("/service")
    public Result<List<LoadBalanceInfo>> getServiceLoadBalanceTypeThroughConsumerName(
            @RequestParam("consumerName")String applicationName
    ){
        List<LoadBalanceInfo> loadBalanceInfoList = loadBalanceService.getProviderListThroughConsumer(applicationName);
        return new Result<>(HttpCode.SUCCESS, "查询成功", loadBalanceInfoList);
    }


//    @PostMapping("/service")
//    public Result<LoadBalanceInfo> getServiceLoadBalanceType(
//            @RequestParam("consumerName")String applicationName,
//            @RequestParam("serviceName") String serviceName,
//            @RequestParam("group") String group,
//            @RequestParam("version") String version
//    ){
//        LoadBalanceInfo serviceLoadBalanceInfo = loadBalanceService.getServiceLoadBalanceInfo(applicationName ,serviceName, group, version);
//        return new Result<>(HttpCode.SUCCESS, "查询负载均衡成功", serviceLoadBalanceInfo);
//    }

    @PostMapping("/remove")
    public Result<LoadBalanceServiceInfoDTO> removeLoadBalance(
            @RequestParam("consumerName")String applicationName,
            @RequestParam("serviceName") String serviceName,
            @RequestParam("group") String group,
            @RequestParam("version") String version
    ){
        loadBalanceService.removeLoadBalance(applicationName ,serviceName, version, group);
        return new Result<>(HttpCode.SUCCESS, "移除负载均衡策略成功，更改为默认的Random", new LoadBalanceServiceInfoDTO(serviceName, group, version, null));
    }

    @PostMapping("/create")
    public Result<LoadBalanceServiceInfoDTO> createLoadBalance(
            @RequestParam("consumerName")String applicationName,
            @RequestParam("serviceName") String serviceName,
            @RequestParam("group") String group,
            @RequestParam("version") String version,
            @RequestParam("loadbalance")String loadBalance
    ){
        loadBalanceService.updateLoadBalance(applicationName ,serviceName, version, group, loadBalance);
        return new Result<>(HttpCode.SUCCESS, "创建负载均衡策略成功", new LoadBalanceServiceInfoDTO(serviceName, group, version, loadBalance));
    }

    @PostMapping("/edit")
    public Result<String> editLoadbalancer(
            @RequestParam("consumerName")String applicationName,
            @RequestParam("serviceName") String serviceName,
            @RequestParam("group") String group,
            @RequestParam("version") String version,
            @RequestParam("loadbalance")String loadBalance
    ){
        loadBalanceService.updateLoadBalance(applicationName ,serviceName, version, group, loadBalance);
        return new Result<>(HttpCode.SUCCESS, "修改负载均衡成功", "服务："+serviceName+" 已更新为："+loadBalance);
    }


}
