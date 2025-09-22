package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.model.TRolePermission;
import org.apache.ibatis.annotations.Param;

public interface TRolePermissionmapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TRolePermission record);

    int insertSelective(TRolePermission record);

    TRolePermission selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TRolePermission record);

    int updateByPrimaryKey(TRolePermission record);

    int deleteByRoleIdAndPermissionId(@Param("roleId") int roleId, @Param("permissionId")int permissionId);
}