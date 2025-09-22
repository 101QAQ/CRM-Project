package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TRole;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.repository.TRolemapper;
import com.duanHaiTao.dongLiYunKe.service.RoleService;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private TRolemapper roleMapper;

    @Override
    public List<TRole> getRolesByUserId(Integer userId){
        return roleMapper.selectRolesByUserId(userId);
    }

    @Override
    public PageInfo<Object> rolePage(Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<TRole> tRoles = roleMapper.selectAll();
        return new PageInfo<>(tRoles);
    }


    @Override
    public int add(TRole role) {
        return roleMapper.insert(role);
    }

    @Override
    public TRole findRole(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(TRole role) {
        return roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public List<TRole> getNotRelatedByUserId(Integer userId) {
        return roleMapper.selectNotRelateRolesByUserId(userId);
    }
}
