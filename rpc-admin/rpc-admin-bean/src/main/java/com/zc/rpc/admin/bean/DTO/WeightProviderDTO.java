package com.zc.rpc.admin.bean.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-19
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeightProviderDTO {

    public String serviceName;

    public String serviceGroup;

    public String version;

    public int weight;



}
