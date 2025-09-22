package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.annotaion.DataFilter;
import com.duanHaiTao.dongLiYunKe.model.TProduct;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TProductmapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TProduct record);

    int insertSelective(TProduct record);

    TProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TProduct record);

    int updateByPrimaryKey(TProduct record);

    TProduct selectByProductName(String productName);

    List<TProduct> selectAll();

    @DataFilter(tableAlias = "tp",tableField = "create_by")
    List<TProduct> selectPage(@Param("filterSql") BaseQuery query,@Param("condition") TProduct condition);
}