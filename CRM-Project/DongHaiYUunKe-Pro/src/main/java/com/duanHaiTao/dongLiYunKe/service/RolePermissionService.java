package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TRolePermission;

public interface RolePermissionService {

    int add(TRolePermission tRolePermission);

    int delete(Integer roleId, Integer permissionId);

}
