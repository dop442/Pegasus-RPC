package com.zc.rpc.admin.bean.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsSerialization implements Serializable {

    public int jdk;

    public int hessian2;

    public int fst;

    public int json;

    public int kryo;

    public int protobuf;

    public int sum;
}
