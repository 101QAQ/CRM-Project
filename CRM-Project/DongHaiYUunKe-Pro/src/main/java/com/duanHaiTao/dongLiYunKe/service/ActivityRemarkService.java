package com.duanHaiTao.dongLiYunKe.service;


import com.duanHaiTao.dongLiYunKe.model.TActivity;
import com.duanHaiTao.dongLiYunKe.model.TActivityRemark;
import com.duanHaiTao.dongLiYunKe.query.QueryRemark;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ActivityRemarkService {

    int addRemark(QueryRemark queryRemark,String token) throws JsonProcessingException;
    List<TActivityRemark> remarkListByActivityId(int activityId);

    PageInfo<Object> remarkPageByActivity(Integer activityId, Integer pageNum);

    TActivityRemark getRemark(int remarkId);

    int updataRemark(QueryRemark queryRemark,String token) throws JsonProcessingException;

    int deleteRemark(int remarkId);
}
