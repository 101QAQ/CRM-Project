package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.Vo.result.ChartData;
import com.duanHaiTao.dongLiYunKe.Vo.result.SummaryStatistic;

import java.util.List;

public interface SummaryStatisticService {

    SummaryStatistic getSummaryStatistics();

    List<ChartData> getFunnelData();
    List <ChartData> getPieData();

    List<ChartData> getActivityMonthCategoryData(int year);

    List<ChartData> getClueByDayCategoryData(String yearMonth);
    List<ChartData> getCustomerByDayCategoryData(String yearMonth);
    List<ChartData> getTranByDayCategoryData(String yearMonth);
}
