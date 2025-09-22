package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.annotaion.DataFilter;
import com.duanHaiTao.dongLiYunKe.model.TTranHistory;
import com.duanHaiTao.dongLiYunKe.model.TTranRemark;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TTranRemarkmapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TTranRemark record);

    int insertSelective(TTranRemark record);

    TTranRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TTranRemark record);

    int updateByPrimaryKey(TTranRemark record);

    @DataFilter(tableAlias = "ttr",tableField = "create_by")
    List<TTranRemark> selectAllByTranId(@Param("filterSql") BaseQuery baseQuery, @Param("tranId") Integer id);

}