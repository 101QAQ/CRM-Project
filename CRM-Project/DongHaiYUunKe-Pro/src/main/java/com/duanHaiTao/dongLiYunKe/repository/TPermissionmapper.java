package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.model.TPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TPermissionmapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TPermission record);

    int insertSelective(TPermission record);

    TPermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TPermission record);

    int updateByPrimaryKey(TPermission record);

    List<TPermission> selectByRoleId(@Param("roleId") List<Integer> roleId);

    List<TPermission> selectPermissionMenuByUserId(Integer userId);

    List<TPermission> selectNotRelatedByRoleId(Integer roleId);


    List<TPermission> selectIsRelatedByRoleId(Integer userId);


}