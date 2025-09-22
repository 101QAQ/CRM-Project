package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TTran;
import com.duanHaiTao.dongLiYunKe.model.TTranHistory;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.query.QueryTran;
import com.duanHaiTao.dongLiYunKe.repository.TTranHistorymapper;
import com.duanHaiTao.dongLiYunKe.repository.TTranmapper;
import com.duanHaiTao.dongLiYunKe.service.TranService;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.duanHaiTao.dongLiYunKe.utils.OrderNumGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;

import java.util.Date;
import java.util.List;

@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private TTranmapper tTranmapper;

    @Autowired
    private TTranHistorymapper tTranHistoryMapper;



    @Override
    public int add(TTran tTran, String token) throws JsonProcessingException {
        TUser tUser = JwtUtil.parseToken(token);
        tTran.setCreateBy(tUser.getId());
        tTran.setTranNo(OrderNumGenerator.generateOrderNumber());
        tTran.setCreateTime(new Date());
        return tTranmapper.insertSelective(tTran);
    }

    @Override
    public PageInfo<Object> tranPage(Integer pageNum,String condition) throws JsonProcessingException {
        PageHelper.startPage(pageNum, 10);
        QueryTran queryTran = new ObjectMapper().readValue(condition, QueryTran.class);

        List<TTran> tTrans = tTranmapper.selectAll(BaseQuery.builder().build(),queryTran);

        return new PageInfo<>(tTrans);
    }

    @Override
    public TTran details(Integer id) throws JsonProcessingException {
        return tTranmapper.selectTranDetailsById(id);
    }

    @Transactional
    @Override
    public int editStage(TTran tran,String token) throws JsonProcessingException {
        TUser tUser = JwtUtil.parseToken(token);

        TTran tranHistory = tTranmapper.selectByPrimaryKey(tran.getId());
        TTranHistory tTranHistory = new TTranHistory();

        tTranHistory.setId(tranHistory.getId());
        tTranHistory.setTranId(tran.getId());
        tTranHistory.setCreateBy(tUser.getId());
        tTranHistory.setCreateTime(new Date());
        tTranHistory.setMoney(tranHistory.getMoney());
        tTranHistory.setStage(tranHistory.getStage());
        tTranHistory.setExpectedDate(tranHistory.getExpectedDate());

        int count=tTranHistoryMapper.insertSelective(tTranHistory);
        count+=tTranmapper.updateByPrimaryKeySelective(tran);
        return count;
    }
}
