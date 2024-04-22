package com.zc.rpc.admin.bean.POJO;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-07
 */
@Data
public class MethodInfo {
    private String methodName;

    private int parametersLength;

    private List<String> parametersType;

    private String returnType;
}
