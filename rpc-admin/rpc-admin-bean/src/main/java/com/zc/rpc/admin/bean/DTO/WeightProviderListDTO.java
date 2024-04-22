package com.zc.rpc.admin.bean.DTO;

import com.zc.rpc.admin.bean.POJO.Provider;
import com.zc.rpc.admin.bean.POJO.ProviderWeight;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeightProviderListDTO implements Serializable {

    String serviceName;

    String serviceGroup;

    String serviceVersion;

    List<ProviderWeight> providerList;



}
