package com.zc.rpc.demo.spring.boot.provider.impl;

import com.zc.rpc.annotation.RpcService;
import com.zc.rpc.demo.api.ProductService;
import com.zc.rpc.demo.spring.boot.provider.POJO.Book;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description
 * @Author Schrodinger's Cobra
 * @Date 2024-04-04
 */
@RpcService(interfaceClass = ProductService.class,
        interfaceClassName = "com.zc.rpc.demo.api.ProductService",
        group = "johnson", weight = 10)
public class ProductServiceImpl implements ProductService {

    private List<Book> bookStock = new ArrayList<>();

    {
        bookStock.add(new Book("教父", 30));
        bookStock.add(new Book("教父", 30));
        bookStock.add(new Book("教父", 30));
        bookStock.add(new Book("鼠疫", 20));
        bookStock.add(new Book("鼠疫", 20));
        bookStock.add(new Book("鼠疫", 20));
        bookStock.add(new Book("鼠疫", 20));
        bookStock.add(new Book("鼠疫", 20));
        bookStock.add(new Book("鼠疫", 20));
        bookStock.add(new Book("鼠疫", 20));
    }


    @Override
    public int queryStock(String productName) {

        long countLong = bookStock.stream().filter(book -> book.getBookName().equals(productName)).count();
        return (int) countLong;
    }

    @Override
    public boolean buyProduct(String productName) {
        if (queryStock(productName)>0){
            Iterator<Book> iterator = bookStock.iterator();
            while (iterator.hasNext()){
                Book book = iterator.next();
                if (book.getBookName().equals(productName)){
                    iterator.remove();
                    break;
                }
            }
            return true;
        }
        return false;
    }


}
