package com.duanHaiTao.dongLiYunKe.controller;


import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TRole;
import com.duanHaiTao.dongLiYunKe.repository.TRolemapper;
import com.duanHaiTao.dongLiYunKe.service.RoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {


    @Autowired
    private RoleService roleService;

    @GetMapping("/getRoles")
    public R<List<TRole>> getRoles(@RequestParam("userId") Integer userId) throws JsonProcessingException {
        List<TRole> roles=roleService.getRolesByUserId(userId);
        return R.Ok(Code.SUCCESS,roles);
    }

    @GetMapping("/rolePage")
    public R<Object> getRolePage(@RequestParam(value = "pageNum",required = false) Integer pageNum) {
        if (pageNum == null) {
            pageNum = 1;
        }
        PageInfo<Object> infor=roleService.rolePage(pageNum);
        return R.Ok(Code.SUCCESS,infor);

    }

    @PostMapping("/add")
    public R<Object> addRole(@RequestBody TRole role) {
        int count=roleService.add(role);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/getRole")
    public R<Object> getRole(@RequestParam("id") Integer id) {
        TRole role=roleService.findRole(id);
        return R.Ok(Code.SUCCESS,role);
    }

    @PutMapping("/edit")
    public R<Object> editRole(@RequestBody TRole role) {
        int count=roleService.update(role);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("/notRelated")
    public R<Object> getNotRelated(@RequestParam("userId") Integer userId) throws JsonProcessingException {

        List<TRole> roles=roleService.getNotRelatedByUserId(userId);
        return R.Ok(Code.SUCCESS,roles);

    }


}
