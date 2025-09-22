package com.duanHaiTao.dongLiYunKe.service.impl;
import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.exceptions.PasswordValidateException;
import com.duanHaiTao.dongLiYunKe.model.TPermission;
import com.duanHaiTao.dongLiYunKe.model.TRole;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.query.QueryRePwd;
import com.duanHaiTao.dongLiYunKe.query.QueryUser;
import com.duanHaiTao.dongLiYunKe.repository.TPermissionmapper;
import com.duanHaiTao.dongLiYunKe.repository.TRolemapper;
import com.duanHaiTao.dongLiYunKe.repository.TUsermapper;
import com.duanHaiTao.dongLiYunKe.service.UserService;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class  UserServiceImpl implements UserDetailsService, UserService {
    @Autowired
    private TUsermapper userMapper;


    @Autowired
    private TRolemapper roleMapper;

    @Autowired
    private TPermissionmapper permissionMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TUser tUser = userMapper.selectByUsername(username);
        if (tUser == null) {
            throw new UsernameNotFoundException(Code.USER_NOT_EXIST.getMsg());
        }
        List<TRole> tRoles = roleMapper.selectRolesByUserId(tUser.getId());
        List<Integer> roleIds = new ArrayList<>();
        tRoles.forEach(role -> roleIds.add(role.getId()));
        List<TPermission> tPermissions=permissionMapper.selectByRoleId(roleIds);
        List<String> authorities = new ArrayList<>();
        tRoles.forEach(role ->authorities.add(role.getRole()));
        tPermissions.forEach(permission ->authorities.add(permission.getCode()));
        tUser.setAuthoritiesStr(authorities);
        return tUser;
    }


    @Override
    public PageInfo<TUser> userPage(Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<TUser> tUsers = userMapper.selectAll(BaseQuery.builder().build());
        return new PageInfo<>(tUsers);
    }

    @Override
    public TUser getUserDetailsById(int id) {
        return userMapper.selectDetailByPrimaryKey(id);
    }

    @PreAuthorize("hasAuthority('user:add')")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addUser(QueryUser queryUser,String token) throws JsonProcessingException {
        TUser tUser = new TUser();
        BeanUtils.copyProperties(queryUser, tUser);
        tUser.setLoginPwd(passwordEncoder.encode(queryUser.getLoginPwd()));
        tUser.setCreateTime(new Date());
        TUser createUser = JwtUtil.parseToken(token);
        tUser.setCreateBy(createUser.getId());
        return userMapper.insertSelective(tUser);
    }



    @PreAuthorize("hasAuthority('user:edit')")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int editUser(QueryUser queryUser, String token) throws JsonProcessingException {
        TUser tUser = new TUser();

        BeanUtils.copyProperties(queryUser, tUser);


        tUser.setEditTime(new Date());
        TUser editUser = JwtUtil.parseToken(token);

        tUser.setEditBy(editUser.getId());

        tUser.setLoginPwd(passwordEncoder.encode(tUser.getLoginPwd()));
        return userMapper.updateByPrimaryKeySelective(tUser);
    }



    @PreAuthorize("hasAuthority('user:delete')")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteUser(Integer id) {
        return userMapper.deleteByPrimaryKey(id);
    }
    @PreAuthorize("hasAuthority('user:delete')")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int deleteBatch(List<String> ids) {
        return userMapper.deleteBatchByPrimaryKey(ids);
    }

    @Override
    public List<TUser> getUsers() {
        return userMapper.selectUserList();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int resetPwd(QueryRePwd queryRePwd, String token) throws JsonProcessingException {
        TUser tUser = JwtUtil.parseToken(token);

        TUser oldUser = userMapper.selectByPrimaryKey(tUser.getId());

        if (!passwordEncoder.matches(queryRePwd.getOldPwd(), oldUser.getPassword())){

            throw new PasswordValidateException("密码验证不正确");

        }

        String newPwd = passwordEncoder.encode(queryRePwd.getNewPwd());

        oldUser.setLoginPwd(newPwd);

        return userMapper.updateByPrimaryKeySelective(oldUser);
    }
}
