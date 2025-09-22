package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.annotaion.DataFilter;
import com.duanHaiTao.dongLiYunKe.model.TActivityRemark;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TActivityRemarkmapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TActivityRemark record);

    int insertSelective(TActivityRemark record);

    TActivityRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TActivityRemark record);

    int updateByPrimaryKey(TActivityRemark record);

    @DataFilter(tableAlias = "tar",tableField = "create_by")
    List<TActivityRemark> selectByActivityId(@Param("filterSql") BaseQuery baseQuery, @Param("activity_id") Integer activityId);

    int deleteByActivityId(Integer activityId);

    int deleteBatchByActivityId(@Param("ids") List<Integer> ids);
}