package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TCustomer;
import com.duanHaiTao.dongLiYunKe.query.ExcelCustomer;
import com.duanHaiTao.dongLiYunKe.query.QueryCustomer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CustomerService {


    int add(QueryCustomer customer,String token) throws JsonProcessingException;


    PageInfo<Object> customerPage(Integer pageNum);

    List<ExcelCustomer> exportExcel(String ids);

    TCustomer customerDetails(Integer id);

}
