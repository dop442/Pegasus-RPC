package com.zc.rpc.demo.spring.boot.provider.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-04
 */
@Data
@AllArgsConstructor
public class Book {

    public String bookName;

    public int price;

}
