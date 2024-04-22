package com.zc.rpc.admin.server.statistics.controller;

import com.zc.rpc.admin.bean.DTO.StatisticsProviderConsumerDTO;
import com.zc.rpc.admin.bean.DTO.StatisticsSerialization;
import com.zc.rpc.admin.server.statistics.service.StatisticsService;
import com.zc.rpc.admin.utils.constants.HttpCode;
import com.zc.rpc.admin.utils.resp.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-19
 */
@RestController
@RequestMapping("statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;


    @GetMapping("/consumerProvider")
    public Result<StatisticsProviderConsumerDTO> getConsumerProvider() throws Exception {
        StatisticsProviderConsumerDTO statisticsProviderConsumerDTO = statisticsService.getConsumerProvider();
        return new Result<>(HttpCode.SUCCESS, "成功", statisticsProviderConsumerDTO);
    }

    @GetMapping("/serialization")
    public Result<StatisticsSerialization> getSerialization(){
        StatisticsSerialization statisticsSerialization = statisticsService.getSerialization();
        return new Result<>(HttpCode.SUCCESS, "成功", statisticsSerialization);
    }



}
