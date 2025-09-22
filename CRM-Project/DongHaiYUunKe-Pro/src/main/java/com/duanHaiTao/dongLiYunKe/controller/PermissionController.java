package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TPermission;
import com.duanHaiTao.dongLiYunKe.service.PermissionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/menu")
    public R<Object> menu(@RequestHeader("Authorization") String token) throws JsonProcessingException {
        List<TPermission> permissionByUserId = permissionService.findPermissionByUserId(token);
        return R.Ok(Code.SUCCESS, permissionByUserId);
    }


    @GetMapping("/notRelated")
    public R<Object> notRelated(@RequestParam("roleId") Integer roleId) {
        List<TPermission> tPermissions=permissionService.findNotRelatedByRoleId(roleId);
        return R.Ok(Code.SUCCESS, tPermissions);
    }


    @GetMapping("/isRelated")
    public R<Object> isRelated(@RequestParam("roleId") Integer roleId,@RequestParam(value = "pageNum",required = false) Integer pageNum) {
        if (pageNum==null){
            pageNum=1;
        }
        PageInfo<Object> infor=permissionService.findIsRelateByRoleId(roleId,pageNum);
        return R.Ok(Code.SUCCESS, infor);
    }

}
