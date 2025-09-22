package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.Vo.result.ChartData;
import com.duanHaiTao.dongLiYunKe.annotaion.DataFilter;
import com.duanHaiTao.dongLiYunKe.model.TActivity;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.query.QueryActivity;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;

public interface TActivitymapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TActivity record);

    int insertSelective(TActivity record);

    TActivity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TActivity record);

    int updateByPrimaryKey(TActivity record);

    @DataFilter(tableAlias = "ta",tableField = "owner_id")
    List<TActivity> selectPage(@Param("filterSql") BaseQuery query, @Param("condition") QueryActivity queryActivity);

    TActivity selectDetails(Integer id);

    List<TActivity> selectActivityByCondition(QueryActivity query);


    List<TActivity> selectAll();


    List<TActivity> selectHeldingActivity(Date date);

    int selectCountAll();

    List<ChartData> selectActivityByMonth(int year);

    int deleteBatchByPrimaryKey(@Param("ids") List<Integer> ids);
}