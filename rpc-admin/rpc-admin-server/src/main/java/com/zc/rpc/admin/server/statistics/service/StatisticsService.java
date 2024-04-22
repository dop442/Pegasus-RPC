package com.zc.rpc.admin.server.statistics.service;

import com.zc.rpc.admin.bean.DTO.StatisticsProviderConsumerDTO;
import com.zc.rpc.admin.bean.DTO.StatisticsSerialization;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-04
 */
public interface StatisticsService {


    StatisticsProviderConsumerDTO getConsumerProvider() throws Exception;


    StatisticsSerialization getSerialization();
}
