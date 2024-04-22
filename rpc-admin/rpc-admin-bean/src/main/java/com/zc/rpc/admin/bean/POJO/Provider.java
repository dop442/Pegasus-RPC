package com.zc.rpc.admin.bean.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Provider {
    String serviceAddr;
    int servicePort;
    int weight;
}
