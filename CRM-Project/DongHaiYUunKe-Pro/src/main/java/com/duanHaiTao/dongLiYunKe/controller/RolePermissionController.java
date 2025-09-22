package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TRolePermission;
import com.duanHaiTao.dongLiYunKe.service.RolePermissionService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rolePermission")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @PostMapping("/add")
    public R<Object> add(@RequestBody TRolePermission tRolePermission) {
        int count=rolePermissionService.add(tRolePermission);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);

    }

    @DeleteMapping("/delete")
    public R<Object> delete(@RequestParam("roleId") Integer roleId, @RequestParam("permissionId") Integer permissionId) {
        int count=rolePermissionService.delete(roleId,permissionId);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

}
