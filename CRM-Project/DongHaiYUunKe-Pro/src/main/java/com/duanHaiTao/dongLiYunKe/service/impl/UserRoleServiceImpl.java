package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TUserRole;
import com.duanHaiTao.dongLiYunKe.repository.TUserRolemapper;
import com.duanHaiTao.dongLiYunKe.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private TUserRolemapper tUserRolemapper;

    @Override
    public int add(TUserRole userRole) {
        return tUserRolemapper.insert(userRole);
    }

    @Override
    public int delete(Integer userId, Integer roleId) {
        return tUserRolemapper.deleteByUserIdAndRoleId(userId,roleId);
    }
}
