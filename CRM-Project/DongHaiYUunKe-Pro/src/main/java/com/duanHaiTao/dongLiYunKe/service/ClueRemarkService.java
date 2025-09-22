package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TClueRemark;
import com.duanHaiTao.dongLiYunKe.query.QueryClueRemark;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

public interface ClueRemarkService {


    int add(QueryClueRemark queryClueRemark,String token) throws JsonProcessingException;


    PageInfo<Object> clueRemarkPage(Integer pageNum);

    TClueRemark findClueRemarkById(int id);

    int update(QueryClueRemark queryClueRemark,String token) throws JsonProcessingException;

    int delete(int id);


}
