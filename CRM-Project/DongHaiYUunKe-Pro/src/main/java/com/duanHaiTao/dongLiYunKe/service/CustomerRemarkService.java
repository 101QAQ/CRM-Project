package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TCustomerRemark;
import com.duanHaiTao.dongLiYunKe.query.QueryCustomerRemark;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

public interface CustomerRemarkService {


    PageInfo<Object> customerRemarkPage(int pageNum,int customerId);

    TCustomerRemark findRemarkById(int id);

    int edit(TCustomerRemark customerRemark,String token) throws JsonProcessingException;

    int delete(int id);

    int add(TCustomerRemark customerRemark,String token) throws JsonProcessingException;
}
