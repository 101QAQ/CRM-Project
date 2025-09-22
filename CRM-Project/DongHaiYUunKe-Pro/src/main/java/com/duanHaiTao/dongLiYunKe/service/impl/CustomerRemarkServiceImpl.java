package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.model.TActivity;
import com.duanHaiTao.dongLiYunKe.model.TCustomerRemark;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.query.QueryActivity;
import com.duanHaiTao.dongLiYunKe.query.QueryCustomerRemark;
import com.duanHaiTao.dongLiYunKe.repository.TCustomerRemarkmapper;
import com.duanHaiTao.dongLiYunKe.service.CustomerRemarkService;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

@Service
public class CustomerRemarkServiceImpl implements CustomerRemarkService {


    @Autowired
    private TCustomerRemarkmapper customerRemarkMapper;

    @Override
    public PageInfo<Object> customerRemarkPage(int customerId,int pageNum) {
        PageHelper.startPage(pageNum, 10);

        List<TCustomerRemark> tCustomerRemarks = customerRemarkMapper.selectAllByCustomerId(BaseQuery.builder().build(), customerId);

        return new PageInfo<>(tCustomerRemarks);
    }

    @Override
    public TCustomerRemark findRemarkById(int id) {
        return customerRemarkMapper.selectDetailsByPrimaryKey(id);
    }

    @Override
    public int edit(TCustomerRemark customerRemark,String token) throws JsonProcessingException {
        TUser tUser = JwtUtil.parseToken(token);
        customerRemark.setEditTime(new Date());
        customerRemark.setEditBy(tUser.getId());
        return customerRemarkMapper.updateByPrimaryKeySelective(customerRemark);
    }

    @Transactional
    @Override
    public int delete(int id) {
        TCustomerRemark customerRemark = customerRemarkMapper.selectByPrimaryKey(id);

        customerRemark.setDeleted(-1);

        return customerRemarkMapper.updateByPrimaryKeySelective(customerRemark);
    }

    @Override
    public int add(TCustomerRemark customerRemark,String token) throws JsonProcessingException {
        TUser tUser = JwtUtil.parseToken(token);

        customerRemark.setCreateBy(tUser.getId());

        customerRemark.setCreateTime(new Date());
        return customerRemarkMapper.insertSelective(customerRemark);
    }


}
