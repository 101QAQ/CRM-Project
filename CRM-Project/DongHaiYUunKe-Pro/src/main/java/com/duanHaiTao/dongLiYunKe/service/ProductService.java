package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TProduct;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import org.apache.commons.math3.stat.descriptive.summary.Product;

import java.util.List;

public interface ProductService {

    List<TProduct> findAll();


    PageInfo<Object> productPage(Integer pageNum, String condition) throws JsonProcessingException;

    int add(TProduct product,String token) throws JsonProcessingException;

    TProduct findOne(Integer id);

    int edit(TProduct product,String token) throws JsonProcessingException;

    int delete(Integer id);

}
