package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.QueryRePwd;
import com.duanHaiTao.dongLiYunKe.query.QueryUser;
import com.duanHaiTao.dongLiYunKe.service.UserService;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/getInfor")
    public R<Authentication> getInfor(Authentication authentication) {
        return R.Ok(Code.SUCCESS,authentication);
    }


    @GetMapping("/isFreeLogin")
    public R<Object> isFreeLogin() {
        return R.Ok(Code.SUCCESS);
    }


    @GetMapping("/toknRefresh")
    public R<Object> toknRefresh(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        String isRemember = request.getHeader("isRemember");
        try {
            TUser tUser=JwtUtil.parseToken(request.getHeader("Authorization"));
            String tokenStr;
            Calendar instance = Calendar.getInstance();
            if (Boolean.parseBoolean(isRemember)){
                instance.add(Calendar.DATE, 7);
                tokenStr=JwtUtil.getToken(tUser,instance);
            }else {
                instance.add(Calendar.SECOND, 60*30);
                tokenStr=JwtUtil.getToken(tUser,instance);
            }
            return R.Ok(Code.TOKEN_REFRESH,tokenStr);
        } catch (JsonProcessingException e) {
            return R.Err(Code.TOKEN_FAIL_REFRESH);
        }
    }


    @GetMapping("/userPage")
    public R<Object> userPage(@RequestParam(value = "pageNum",required = false) Integer pageNum) {
        if (pageNum == null) {
            pageNum = 1;
        }
        PageInfo<TUser> tUserPageInfo = userService.userPage(pageNum);
        return R.Ok(Code.SUCCESS,tUserPageInfo);
    }


    @GetMapping("/details")
    public R<TUser> details(@RequestParam("id") Integer id) {
        TUser user = userService.getUserDetailsById(id);
        return R.Ok(Code.SUCCESS,user);
    }


    @GetMapping("/usersList")
    public R<List<TUser>> usersList() {
        List<TUser> users = userService.getUsers();
        return R.Ok(Code.SUCCESS,users);
    }


    @PostMapping("/add")
    public R<Object> add(@RequestBody QueryUser queryUser,@RequestHeader("Authorization") String token) {
        int count;
        try {
            count = userService.addUser(queryUser,token);
        } catch (JsonProcessingException e) {
            return R.Err(Code.TOKEN_FAIL_PROCESSING);
        }
        if (count>0){
            return R.Ok(Code.SUCCESS);
        }else {
            return R.Err(Code.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/edit")
    public R<Object> edit(@RequestBody QueryUser queryUser,@RequestHeader("Authorization") String token) {
        int count;
        try {
            count = userService.editUser(queryUser,token);
        } catch (JsonProcessingException e) {
            return R.Err(Code.TOKEN_FAIL_PROCESSING);
        }
        if (count>0){
            return R.Ok(Code.SUCCESS);
        }else {
            return R.Err(Code.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/delete")
    public R<Object> delete(Integer id) {
        int count=userService.deleteUser(id);
        if (count>0){
            return R.Ok(Code.SUCCESS);
        }else {
            return R.Err(Code.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteBatch")
    public R<Object> batchDelete(@RequestParam("ids") String ids) {
        String[] split = ids.split(",");
        List<String> userIds = Arrays.asList(split);
        int count=userService.deleteBatch(userIds);
        if (count>0){
            return R.Ok(Code.SUCCESS,count);
        }else {
            return R.Err(Code.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping("/resetPwd")
    public R<Object> editPwd(@RequestBody QueryRePwd queryRePwd,@RequestHeader("Authorization") String  token) throws JsonProcessingException {
        int count=userService.resetPwd(queryRePwd,token);
        return count>0? R.Ok(Code.SUCCESS): R.Err(Code.INTERNAL_SERVER_ERROR);
    }


}
