package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.Vo.result.ChartData;
import com.duanHaiTao.dongLiYunKe.Vo.result.SummaryStatistic;
import com.duanHaiTao.dongLiYunKe.repository.TActivitymapper;
import com.duanHaiTao.dongLiYunKe.repository.TCluemapper;
import com.duanHaiTao.dongLiYunKe.repository.TCustomermapper;
import com.duanHaiTao.dongLiYunKe.repository.TTranmapper;
import com.duanHaiTao.dongLiYunKe.service.SummaryStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SummaryStatisticServiceImpl implements SummaryStatisticService {

    @Autowired
    private TActivitymapper activityMapper;
    @Autowired
    private TCustomermapper customerMapper;
    @Autowired
    private TCluemapper clueMapper;
    @Autowired
    private TTranmapper tranMapper;

    @Override
    public SummaryStatistic getSummaryStatistics() {
        int heldActivity = activityMapper.selectHeldingActivity(new Date()).size();
        int activitySum = activityMapper.selectCountAll();
        int clueSum = clueMapper.selectClueSum();
        int customerSum = customerMapper.customerSum();
        int successMoneySum = tranMapper.selectSuccessTranMoneySum();
        int moneySum = tranMapper.selectTranMoneySum();
        return SummaryStatistic.builder().heldActivityAndSum(heldActivity+"/"+activitySum)
                .clueSum(clueSum)
                .customerSum(customerSum)
                .transactionSum(successMoneySum+"/"+moneySum).build();
    }

    @Override
    public List<ChartData> getFunnelData() {
        List<ChartData> funnelDatas = new ArrayList<ChartData>();
        int clueSum = clueMapper.selectClueSum();
        funnelDatas.add(ChartData.builder().value(clueSum).name("线索").build());
        int customerSum = customerMapper.customerSum();
        funnelDatas.add(ChartData.builder().value(customerSum).name("客户").build());
        int tranNum = tranMapper.selectTranNum();
        funnelDatas.add(ChartData.builder().value(tranNum).name("交易").build());
        int successTranNum = tranMapper.selectSuccessTranNum();
        funnelDatas.add(ChartData.builder().value(successTranNum).name("成交").build());
        return funnelDatas;
    }

    @Override
    public List<ChartData> getPieData() {
        return clueMapper.selectClueSourceNum();
    }

    @Override
    public List<ChartData> getActivityMonthCategoryData(int year) {
        return activityMapper.selectActivityByMonth(year);
    }

    @Override
    public List<ChartData> getClueByDayCategoryData(String yearMonth) {
        return clueMapper.selectClueByDay(yearMonth);
    }

    @Override
    public List<ChartData> getCustomerByDayCategoryData(String yearMonth) {
        return customerMapper.selectCustomerByDay(yearMonth);
    }

    @Override
    public List<ChartData> getTranByDayCategoryData(String yearMonth) {
        return tranMapper.selectTranByDay(yearMonth);
    }
}
