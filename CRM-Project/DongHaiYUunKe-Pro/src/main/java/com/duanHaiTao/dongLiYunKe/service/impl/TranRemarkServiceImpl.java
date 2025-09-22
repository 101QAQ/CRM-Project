package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TTranRemark;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.repository.TTranRemarkmapper;
import com.duanHaiTao.dongLiYunKe.service.TranRemarkService;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

import java.util.List;

@Service
public class TranRemarkServiceImpl implements TranRemarkService {


    @Autowired
    private TTranRemarkmapper tTranRemarkmapper;


    @Override
    public PageInfo<Object> remarkPage(Integer pageNum, Integer tranId) {
        PageHelper.startPage(pageNum, 10);
        List<TTranRemark> tTranRemarks = tTranRemarkmapper.selectAllByTranId(BaseQuery.builder().build(), tranId);
        return new PageInfo<>(tTranRemarks);
    }

    @Override
    public int add(TTranRemark tTranRemark, String token) throws JsonProcessingException {
        TUser tUser = JwtUtil.parseToken(token);
        tTranRemark.setCreateBy(tUser.getId());
        tTranRemark.setCreateTime(new Date());
        return tTranRemarkmapper.insertSelective(tTranRemark);
    }
}
