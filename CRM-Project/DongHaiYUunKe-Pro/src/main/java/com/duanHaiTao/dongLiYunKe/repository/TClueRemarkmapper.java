package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.annotaion.DataFilter;
import com.duanHaiTao.dongLiYunKe.model.TClueRemark;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TClueRemarkmapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TClueRemark record);

    int insertSelective(TClueRemark record);

    TClueRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TClueRemark record);

    int updateByPrimaryKey(TClueRemark record);

    @DataFilter(tableAlias = "tcr",tableField = "create_by")
    List<TClueRemark> selectForPage(BaseQuery query);

    TClueRemark selectDetailByPrimaryKey(Integer id);

    int deleteByClueId(Integer id);

    int deleteBatchByClueIds(@Param("ids") List<Integer> ids);

}