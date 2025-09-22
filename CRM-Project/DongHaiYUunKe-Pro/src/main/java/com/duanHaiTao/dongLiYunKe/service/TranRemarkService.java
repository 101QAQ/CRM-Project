package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TTranRemark;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

public interface TranRemarkService {

    PageInfo<Object> remarkPage(Integer pageNum, Integer tranId);

    int add(TTranRemark tTranRemark, String token) throws JsonProcessingException;

}
