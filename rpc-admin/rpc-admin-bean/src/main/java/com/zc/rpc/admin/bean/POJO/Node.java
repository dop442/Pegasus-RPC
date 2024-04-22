package com.zc.rpc.admin.bean.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-03-19
 */
@Data
@AllArgsConstructor
public class Node{
    public String id;
    public String text;


    @Override
    public int hashCode(){
        return Objects.hash(text, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if (obj==null||getClass()!= obj.getClass()) return false;
        Node node = (Node) obj;
        return id.equals(node.id) && text.equals(node.text);
    }
}
