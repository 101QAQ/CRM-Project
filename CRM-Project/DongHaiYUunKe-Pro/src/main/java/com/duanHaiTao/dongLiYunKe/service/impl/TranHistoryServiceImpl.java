package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TTran;
import com.duanHaiTao.dongLiYunKe.model.TTranHistory;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.query.QueryTran;
import com.duanHaiTao.dongLiYunKe.repository.TTranHistorymapper;
import com.duanHaiTao.dongLiYunKe.service.TranHistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranHistoryServiceImpl implements TranHistoryService {

    @Autowired
    private TTranHistorymapper tranHistoryMapper;

    @Override
    public PageInfo<Object> historyPage(int pageNum,Integer tranId) {
        PageHelper.startPage(pageNum, 10);

        List<TTranHistory> tranHistory = tranHistoryMapper.selectAll(BaseQuery.builder().build(),tranId);

        return new PageInfo<>(tranHistory);
    }
}
