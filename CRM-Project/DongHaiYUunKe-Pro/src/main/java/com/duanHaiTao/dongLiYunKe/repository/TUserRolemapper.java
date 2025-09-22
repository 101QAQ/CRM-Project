package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.model.TUserRole;
import org.apache.ibatis.annotations.Param;

public interface TUserRolemapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TUserRole record);

    int insertSelective(TUserRole record);

    TUserRole selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TUserRole record);

    int updateByPrimaryKey(TUserRole record);

    int deleteByUserIdAndRoleId(@Param("userId") Integer userId, @Param("roleId")Integer roleId);
}