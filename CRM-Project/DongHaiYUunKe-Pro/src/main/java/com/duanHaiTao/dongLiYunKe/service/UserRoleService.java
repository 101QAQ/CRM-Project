package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TUserRole;

public interface UserRoleService {

    int add(TUserRole userRole);

    int delete(Integer userId, Integer roleId);

}
