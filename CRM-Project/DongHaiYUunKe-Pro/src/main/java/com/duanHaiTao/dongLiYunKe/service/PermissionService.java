package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TPermission;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface PermissionService {

    List<TPermission> findPermissionByUserId(String token) throws JsonProcessingException;


    List<TPermission> findNotRelatedByRoleId(Integer roleId);

    PageInfo<Object> findIsRelateByRoleId(Integer roleId, Integer pageNum);

}
