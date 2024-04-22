package com.zc.rpc.admin.bean.DTO;

import com.zc.rpc.admin.bean.POJO.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadBalanceInfo {
    public String serviceName;
    public String serviceGroup;
    public String serviceVersion;
    public String loadBalanceType;
    public List<Provider> providerList;
}
