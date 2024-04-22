package com.zc.rpc.admin.bean.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-19
 */
@Data
@AllArgsConstructor
public class Line {
    public String from;
    public String to;

    @Override
    public int hashCode(){
        return Objects.hash(from, to);
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (obj==null||getClass()!= obj.getClass()) return false;
        Line line = (Line) obj;
        return from.equals(line.from) && to.equals(line.to);
    }
}
