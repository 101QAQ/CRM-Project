package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.Vo.result.ChartData;
import com.duanHaiTao.dongLiYunKe.annotaion.DataFilter;
import com.duanHaiTao.dongLiYunKe.model.TTran;
import com.duanHaiTao.dongLiYunKe.model.TTranHistory;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.query.QueryTran;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TTranmapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TTran record);

    int insertSelective(TTran record);

    TTran selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TTran record);

    int updateByPrimaryKey(TTran record);

    int selectSuccessTranMoneySum();

    int selectTranMoneySum();

    int selectTranNum();

    int selectSuccessTranNum();
    List<ChartData> selectTranByDay(String yearMonth);

    @DataFilter(tableAlias = "tt",tableField = "create_by")
    List<TTran> selectAll(@Param("filterSql") BaseQuery baseQuery,@Param("condition") QueryTran queryTran);

    TTran selectTranDetailsById(Integer id);
}