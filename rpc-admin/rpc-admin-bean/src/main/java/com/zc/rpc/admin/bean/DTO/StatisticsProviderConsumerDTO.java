package com.zc.rpc.admin.bean.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsProviderConsumerDTO {

    public int providerCount;

    public int consumerCount;


}
