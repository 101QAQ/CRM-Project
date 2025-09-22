package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.Vo.result.ChartData;
import com.duanHaiTao.dongLiYunKe.annotaion.DataFilter;
import com.duanHaiTao.dongLiYunKe.model.TCustomer;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TCustomermapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TCustomer record);

    int insertSelective(TCustomer record);

    TCustomer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TCustomer record);

    int updateByPrimaryKey(TCustomer record);

    @DataFilter(tableAlias = "tc",tableField = "create_by")
    List<TCustomer> selectPage(BaseQuery query);
    @DataFilter(tableAlias = "tc",tableField = "create_by")
    List<TCustomer> selectAll(@Param("filterSql") BaseQuery query, @Param("ids") List<Integer> ids);

    int customerSum();

    List<ChartData> selectCustomerByDay(String yearMonth);

    TCustomer selectDetailsByPrimaryKey(Integer id);

}