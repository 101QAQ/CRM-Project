package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TTran;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

public interface TranService {


    int add(TTran tTran,String token) throws JsonProcessingException;

    PageInfo<Object> tranPage(Integer pageNum,String condition) throws JsonProcessingException;

    TTran details(Integer id) throws JsonProcessingException;

    int editStage(TTran tran,String token) throws JsonProcessingException;
}
