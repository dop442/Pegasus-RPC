package com.zc.rpc.admin.bean.DTO;

import com.zc.rpc.protocol.meta.ServiceInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-06
 */
@Data
@Builder
public class ServiceListDTO {

    public List<ServiceInfo> serviceList;

    public int count;
}
