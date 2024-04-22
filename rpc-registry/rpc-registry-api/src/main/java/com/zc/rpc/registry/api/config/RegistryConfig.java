package com.zc.rpc.registry.api.config;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-02-08
 */

@Getter
@Setter
@AllArgsConstructor
public class RegistryConfig implements Serializable {

    private static final long serialVersionUID = -7248658103788758893L;

    private String registryAddr;

    private String registryType;

    private String registryLoadBalanceType;

}
