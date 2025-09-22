package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface RoleService {

    List<TRole> getRolesByUserId(Integer userId) throws JsonProcessingException;

    PageInfo<Object> rolePage(Integer pageNum);

    int add(TRole role);

    TRole findRole(Integer id);

    int update(TRole role);

    List<TRole> getNotRelatedByUserId(Integer userId);

}
