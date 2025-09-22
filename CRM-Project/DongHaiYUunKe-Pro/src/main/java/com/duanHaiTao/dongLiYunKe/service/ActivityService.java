package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TActivity;
import com.duanHaiTao.dongLiYunKe.query.QueryActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ActivityService {


    PageInfo<Object> activityPage(Integer pageNum,QueryActivity queryActivity);

    TActivity activityDetails(int id);

    int addActivity(QueryActivity queryActivity,String token) throws JsonProcessingException;

    int updateActivity(QueryActivity queryActivity,String token) throws JsonProcessingException;

    List<TActivity> findAll();

    int delete(int id);

    int deleteBatch(String idStr);


}
