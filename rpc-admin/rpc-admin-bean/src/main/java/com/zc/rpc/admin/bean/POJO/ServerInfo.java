package com.zc.rpc.admin.bean.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-08
 */
@Data
@AllArgsConstructor
public class ServerInfo {
    String serviceName;

    String version;

    String group;

}
