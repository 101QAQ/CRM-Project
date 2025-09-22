package com.duanHaiTao.dongLiYunKe.service;

import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.QueryRePwd;
import com.duanHaiTao.dongLiYunKe.query.QueryUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {

    PageInfo<TUser> userPage(Integer pageNum);

    TUser getUserDetailsById(int id);

    int addUser(QueryUser queryUser,String token) throws JsonProcessingException;

    int editUser(QueryUser queryUser,String token) throws JsonProcessingException;

    int deleteUser(Integer id);

    int deleteBatch(List<String> ids);

    List<TUser> getUsers();

    int resetPwd(QueryRePwd queryRePwd,String token) throws JsonProcessingException;

}
