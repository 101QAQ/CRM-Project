package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.Vo.result.ChartData;
import com.duanHaiTao.dongLiYunKe.annotaion.DataFilter;
import com.duanHaiTao.dongLiYunKe.model.TClue;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.query.QueryClue;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TCluemapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TClue record);

    int insertSelective(TClue record);

    TClue selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TClue record);

    int updateByPrimaryKey(TClue record);

    @DataFilter(tableAlias = "tc",tableField = "owner_id")
    List<TClue> selectAll(@Param("filterSql") BaseQuery baseQuery, @Param("queryClue") QueryClue queryClue);

    int insertBatch(@Param("clues") List<TClue> list);


    int deleteBatch(@Param("ids") List<Integer> ids);

    TClue selectDetail(Integer id);


    int selectPhoneNum(String phoneNum);


    int selectClueSum();

    List<ChartData> selectClueSourceNum();

    List<ChartData> selectClueByDay(String yearMonth);


}