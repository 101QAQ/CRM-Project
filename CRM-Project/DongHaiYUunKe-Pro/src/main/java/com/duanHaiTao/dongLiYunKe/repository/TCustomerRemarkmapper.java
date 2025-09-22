package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.annotaion.DataFilter;
import com.duanHaiTao.dongLiYunKe.model.TCustomerRemark;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TCustomerRemarkmapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TCustomerRemark record);

    int insertSelective(TCustomerRemark record);

    TCustomerRemark selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TCustomerRemark record);

    int updateByPrimaryKey(TCustomerRemark record);

    @DataFilter(tableAlias = "cr",tableField ="create_by" )
    List<TCustomerRemark> selectAllByCustomerId(@Param("filterSql") BaseQuery baseQuery, @Param("customerId") int customerId);

    TCustomerRemark selectDetailsByPrimaryKey(int id);

}