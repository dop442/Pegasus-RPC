package com.zc.rpc.admin.bean.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-11
 */
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {
    public List<T> pageList;

    public int count;

}
