package com.zc.rpc.admin.bean.DTO;

import com.zc.rpc.admin.bean.POJO.Provider;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-06
 */
@Data
@Builder
public class ServiceDetailInfoDTO implements Serializable {
    private static final long serialVersionUID = 8743051602150051909L;

    public String serviceName;

    public String serviceGroup;

    public String serviceVersion;

    public List<Provider> providerList;
}
