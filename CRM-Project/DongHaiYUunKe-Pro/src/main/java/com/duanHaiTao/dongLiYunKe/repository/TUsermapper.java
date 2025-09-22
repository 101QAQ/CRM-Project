package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.annotaion.DataFilter;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TUsermapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

    TUser selectByUsername(String username);

    TUser selectDetailByPrimaryKey(Integer id);

    @DataFilter(tableAlias = "tu",tableField = "id")
    List<TUser> selectAll(BaseQuery query);

    List<TUser> selectUserList();

    int deleteBatchByPrimaryKey(@Param("ids") List<String> ids);
}