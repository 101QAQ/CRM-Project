package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TPermission;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.repository.TPermissionmapper;
import com.duanHaiTao.dongLiYunKe.service.PermissionService;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Permission;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private TPermissionmapper permissionMapper;

    @Override
    public List<TPermission> findPermissionByUserId(String token) throws JsonProcessingException {
        TUser tUser = JwtUtil.parseToken(token);
        return permissionMapper.selectPermissionMenuByUserId(tUser.getId());
    }

    @Override
    public List<TPermission> findNotRelatedByRoleId(Integer roleId) {
        return permissionMapper.selectNotRelatedByRoleId(roleId);
    }

    @Override
    public PageInfo<Object> findIsRelateByRoleId(Integer roleId, Integer pageNum) {
        PageHelper.startPage(pageNum, 10);

        List<TPermission> tPermissions = permissionMapper.selectIsRelatedByRoleId(roleId);

        return new PageInfo<>(tPermissions);

    }
}
