package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TActivityRemark;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.query.QueryRemark;
import com.duanHaiTao.dongLiYunKe.repository.TActivityRemarkmapper;
import com.duanHaiTao.dongLiYunKe.service.ActivityRemarkService;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Autowired
    private TActivityRemarkmapper tActivityRemarkmapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addRemark(QueryRemark queryRemark,String token) throws JsonProcessingException {
        TActivityRemark tActivityRemark = new TActivityRemark();
        BeanUtils.copyProperties(queryRemark,tActivityRemark);
        TUser tUser = JwtUtil.parseToken(token);
        tActivityRemark.setCreateTime(new Date());
        tActivityRemark.setCreateBy(tUser.getId());
        return tActivityRemarkmapper.insertSelective(tActivityRemark);
    }

    @Override
    public List<TActivityRemark> remarkListByActivityId(int activityId) {
        return tActivityRemarkmapper.selectByActivityId(BaseQuery.builder().build(),activityId);
    }

    @Override
    public PageInfo<Object> remarkPageByActivity(Integer activityId, Integer pageNum) {
        PageHelper.startPage(pageNum,10);
        List<TActivityRemark> tActivityRemarks = tActivityRemarkmapper.selectByActivityId(BaseQuery.builder().build(), activityId);
        return new PageInfo<>(tActivityRemarks);
    }

    @Override
    public TActivityRemark getRemark(int remarkId) {
        return tActivityRemarkmapper.selectByPrimaryKey(remarkId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updataRemark(QueryRemark queryRemark, String token) throws JsonProcessingException {
        TActivityRemark tActivityRemark = new TActivityRemark();
        BeanUtils.copyProperties(queryRemark,tActivityRemark);
        TUser tUser = JwtUtil.parseToken(token);
        tActivityRemark.setEditTime(new Date());
        tActivityRemark.setEditBy(tUser.getId());
        return tActivityRemarkmapper.updateByPrimaryKeySelective(tActivityRemark);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteRemark(int remarkId) {
        TActivityRemark tActivityRemark = new TActivityRemark();
        tActivityRemark.setId(remarkId);
        tActivityRemark.setDeleted(1);
        return tActivityRemarkmapper.updateByPrimaryKeySelective(tActivityRemark);
    }
}
