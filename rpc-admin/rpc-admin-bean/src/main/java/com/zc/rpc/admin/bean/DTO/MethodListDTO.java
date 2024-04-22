package com.zc.rpc.admin.bean.DTO;

import com.zc.rpc.admin.bean.POJO.MethodInfo;
import com.zc.rpc.protocol.meta.ServiceMethodMeta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MethodListDTO {

    List<ServiceMethodMeta> methodInfoList;

    int methodCount;

}
