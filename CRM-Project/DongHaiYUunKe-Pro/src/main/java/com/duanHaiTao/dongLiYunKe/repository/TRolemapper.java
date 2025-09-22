package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.model.TRole;

import java.util.List;

public interface TRolemapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TRole record);

    int insertSelective(TRole record);

    TRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TRole record);

    int updateByPrimaryKey(TRole record);

    List<TRole> selectRolesByUserId(Integer userId);


    List<TRole> selectAll();

    List<TRole> selectNotRelateRolesByUserId(Integer userId);

}