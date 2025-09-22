package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.model.TUserRole;
import com.duanHaiTao.dongLiYunKe.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userRole")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;


    @PostMapping("/add")
    public R<Object> add(@RequestBody TUserRole userRole) {
        int count=userRoleService.add(userRole);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);

    }
    
    @DeleteMapping("/delete")
    public R<Object> delete(@RequestParam("userId") Integer userId, @RequestParam("roleId") Integer roleId) {
        int count=userRoleService.delete(userId,roleId);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

}
