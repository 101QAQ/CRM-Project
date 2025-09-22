package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.ChartData;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.Vo.result.SummaryStatistic;
import com.duanHaiTao.dongLiYunKe.service.SummaryStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/summary")
public class SummaryDataController {


    @Autowired
    private SummaryStatisticService summaryStatisticService;

    @GetMapping("/summaryData")
    public R<SummaryStatistic> summaryData() {
       return R.Ok(Code.SUCCESS,summaryStatisticService.getSummaryStatistics());
    }


    @GetMapping("/FunnelData")
    public R<List<ChartData>> FunnelData() {
        return R.Ok(Code.SUCCESS,summaryStatisticService.getFunnelData());
    }

    @GetMapping("/PieData")
    public R<List<ChartData>> PieData() {
        return R.Ok(Code.SUCCESS,summaryStatisticService.getPieData());
    }


    @GetMapping("/activityMonthCategory")
    public R<List<ChartData>> activityMonthCategory(@RequestParam(value = "year",required = false) Integer year) {
        if (year==null){
            year=Calendar.getInstance().get(Calendar.YEAR);
        }
        List<ChartData> monthData=summaryStatisticService.getActivityMonthCategoryData(year);
        return R.Ok(Code.SUCCESS,monthData);
    }

    @GetMapping("/clueDayCategory")
    public R<List<ChartData>> clueDayCategory(@RequestParam(value = "yearMonth",required = false) String yearMonth) {
        if (yearMonth==null){
            yearMonth=new SimpleDateFormat("yyyy-MM").format(new Date());
        }
        List<ChartData> clueByDayCategoryData = summaryStatisticService.getClueByDayCategoryData(yearMonth);
        return R.Ok(Code.SUCCESS,clueByDayCategoryData);
    }

    @GetMapping("/customerDayCategory")
    public R<List<ChartData>> customerDayCategory(@RequestParam(value = "yearMonth",required = false) String yearMonth) {
        if (yearMonth==null){
            yearMonth=new SimpleDateFormat("yyyy-MM").format(new Date());
        }
        List<ChartData> customerByDayCategoryData = summaryStatisticService.getCustomerByDayCategoryData(yearMonth);
        return R.Ok(Code.SUCCESS,customerByDayCategoryData);
    }

    @GetMapping("/tranDayCategory")
    public R<List<ChartData>> tranDayCategory(@RequestParam(value = "yearMonth",required = false) String yearMonth) {
        if (yearMonth==null){
            yearMonth=new SimpleDateFormat("yyyy-MM").format(new Date());
        }
        List<ChartData> tranByDayCategoryData = summaryStatisticService.getTranByDayCategoryData(yearMonth);
        return R.Ok(Code.SUCCESS,tranByDayCategoryData);
    }
}
