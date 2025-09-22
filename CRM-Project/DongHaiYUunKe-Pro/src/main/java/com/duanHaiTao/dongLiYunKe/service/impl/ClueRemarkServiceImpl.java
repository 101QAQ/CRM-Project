package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TActivityRemark;
import com.duanHaiTao.dongLiYunKe.model.TClue;
import com.duanHaiTao.dongLiYunKe.model.TClueRemark;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.query.QueryClueRemark;
import com.duanHaiTao.dongLiYunKe.repository.TClueRemarkmapper;
import com.duanHaiTao.dongLiYunKe.service.ClueRemarkService;
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
public class ClueRemarkServiceImpl implements ClueRemarkService {

    @Autowired
    private TClueRemarkmapper tClueRemarkmapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int add(QueryClueRemark queryClueRemark,String token) throws JsonProcessingException {
        TClueRemark tClueRemark = new TClueRemark();
        tClueRemark.setCreateTime(new Date());
        TUser tUser = JwtUtil.parseToken(token);
        tClueRemark.setCreateBy(tUser.getId());
        BeanUtils.copyProperties(queryClueRemark, tClueRemark);
        return tClueRemarkmapper.insertSelective(tClueRemark);
    }

    @Override
    public PageInfo<Object> clueRemarkPage(Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<TClueRemark> tClues = tClueRemarkmapper.selectForPage(BaseQuery.builder().build());
        return new PageInfo<>(tClues);
    }

    @Override
    public TClueRemark findClueRemarkById(int id) {
        return tClueRemarkmapper.selectDetailByPrimaryKey(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(QueryClueRemark queryClueRemark, String token) throws JsonProcessingException {
        TClueRemark tClueRemark = new TClueRemark();
        BeanUtils.copyProperties(queryClueRemark,tClueRemark);
        TUser tUser = JwtUtil.parseToken(token);
        tClueRemark.setEditTime(new Date());
        tClueRemark.setEditBy(tUser.getId());
        return tClueRemarkmapper.updateByPrimaryKeySelective(tClueRemark);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int delete(int id) {
        TClueRemark tClueRemark = new TClueRemark();
        tClueRemark.setId(id);
        tClueRemark.setDeleted(1);
        return tClueRemarkmapper.updateByPrimaryKeySelective(tClueRemark);
    }


}
