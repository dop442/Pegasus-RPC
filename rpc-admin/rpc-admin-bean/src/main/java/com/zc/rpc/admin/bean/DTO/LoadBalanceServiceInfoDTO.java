package com.zc.rpc.admin.bean.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoadBalanceServiceInfoDTO {

    public String serviceName;
    public String group;
    public String version;
    public String loadBalance;

}
