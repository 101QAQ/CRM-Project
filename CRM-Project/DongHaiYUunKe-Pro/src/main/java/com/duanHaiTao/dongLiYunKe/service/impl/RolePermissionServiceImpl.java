package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TRolePermission;
import com.duanHaiTao.dongLiYunKe.repository.TRolePermissionmapper;
import com.duanHaiTao.dongLiYunKe.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    @Autowired
    private TRolePermissionmapper rolePermissionMapper;

    @Override
    public int add(TRolePermission tRolePermission) {
        return rolePermissionMapper.insert(tRolePermission);
    }

    @Override
    public int delete(Integer roleId, Integer permissionId) {
        return rolePermissionMapper.deleteByRoleIdAndPermissionId(roleId, permissionId);
    }
}
