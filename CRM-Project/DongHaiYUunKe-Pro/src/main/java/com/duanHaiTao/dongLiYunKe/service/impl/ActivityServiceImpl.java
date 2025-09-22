package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TActivity;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.query.QueryActivity;
import com.duanHaiTao.dongLiYunKe.repository.TActivityRemarkmapper;
import com.duanHaiTao.dongLiYunKe.repository.TActivitymapper;
import com.duanHaiTao.dongLiYunKe.repository.TUsermapper;
import com.duanHaiTao.dongLiYunKe.service.ActivityService;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private TActivitymapper activitymapper;

    @Autowired
    private TActivityRemarkmapper  activityRemarkMapper;

    @Override
    public PageInfo<Object> activityPage(Integer pageNum,QueryActivity queryActivity) {
        PageHelper.startPage(pageNum, 10);
        List<TActivity> tActivities = activitymapper.selectPage(BaseQuery.builder().build(),queryActivity);
        return new PageInfo<>(tActivities);
    }

    @Override
    public TActivity activityDetails(int id) {
        return activitymapper.selectDetails(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasAnyAuthority('activity:add')")
    @Override
    public int addActivity(QueryActivity queryActivity,String token) throws JsonProcessingException {
        TActivity tActivity = new TActivity();
        BeanUtils.copyProperties(queryActivity, tActivity);
        tActivity.setCreateTime(new Date());
        TUser tUser = JwtUtil.parseToken(token);
        tActivity.setCreateBy(tUser.getId());
        return activitymapper.insertSelective(tActivity);
    }

    @Transactional(rollbackFor = Exception.class)
    @PreAuthorize("hasAnyAuthority('activity:edit')")
    @Override
    public int updateActivity(QueryActivity queryActivity, String token) throws JsonProcessingException {
        TActivity tActivity = new TActivity();
        BeanUtils.copyProperties(queryActivity, tActivity);
        tActivity.setEditTime(new Date());
        TUser tUser = JwtUtil.parseToken(token);
        tActivity.setEditBy(tUser.getId());
        return activitymapper.updateByPrimaryKeySelective(tActivity);
    }

    @Override
    public List<TActivity> findAll() {
        return activitymapper.selectAll();
    }

    @PreAuthorize("hasAuthority('activity:delete')")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int delete(int id) {
        int i = activityRemarkMapper.deleteByActivityId(id);
        i+=activitymapper.deleteByPrimaryKey(id);
        return i;
    }

    @PreAuthorize("hasAuthority('activity:delete')")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteBatch(String idStr) {
        List<String> list = Arrays.asList(idStr.split(","));

        List<Integer> ids=new ArrayList<>();

        list.forEach(id->ids.add(Integer.parseInt(id)));

        activityRemarkMapper.deleteBatchByActivityId(ids);

        return activitymapper.deleteBatchByPrimaryKey(ids);
    }


}
