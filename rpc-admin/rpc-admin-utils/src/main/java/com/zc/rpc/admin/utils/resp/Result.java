package com.zc.rpc.admin.utils.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1497405107265595284L;

    private Integer code;

    private String codeMsg;

    private T data;
}
