package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.exceptions.CustomExistException;
import com.duanHaiTao.dongLiYunKe.model.TClue;
import com.duanHaiTao.dongLiYunKe.model.TCustomer;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.query.ExcelCustomer;
import com.duanHaiTao.dongLiYunKe.query.QueryCustomer;
import com.duanHaiTao.dongLiYunKe.repository.TCluemapper;
import com.duanHaiTao.dongLiYunKe.repository.TCustomermapper;
import com.duanHaiTao.dongLiYunKe.service.CustomerService;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private TCustomermapper customerMapper;

    @Autowired
    private TCluemapper clueMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int add(QueryCustomer customer,String token) throws JsonProcessingException {

        if (clueMapper.selectByPrimaryKey(customer.getClueId()).getState()>0) {
            TCustomer tCustomer = new TCustomer();
            BeanUtils.copyProperties(customer,tCustomer);
            TUser tUser = JwtUtil.parseToken(token);
            tCustomer.setCreateBy(tUser.getId());
            tCustomer.setCreateTime(new Date());

            TClue tClue = new TClue();
            tClue.setId(customer.getClueId());
            tClue.setState(-1);
            int count=clueMapper.updateByPrimaryKeySelective(tClue);
            count+=customerMapper.insertSelective(tCustomer);
            return count;
        }else{
            throw new CustomExistException("客户已经存在");
        }
    }

    @Override
    public PageInfo<Object> customerPage(Integer pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<TCustomer> tCustomers = customerMapper.selectPage(BaseQuery.builder().build());
        return new PageInfo<>(tCustomers);
    }
    @PreAuthorize("hasAuthority('customer:export')")
    @Override
    public List<ExcelCustomer> exportExcel(String ids) {
        List<Integer> idList=new ArrayList<>();
        if (ids!=null){
            Arrays.asList(ids.split(",")).forEach(id->idList.add(Integer.parseInt(id)));
        }
        List<TCustomer> tCustomers = customerMapper.selectAll(BaseQuery.builder().build(), idList);
        List<ExcelCustomer> excelCustomers = new ArrayList<>();
        tCustomers.forEach(customer->{
            ExcelCustomer excelCustomer = new ExcelCustomer();
            excelCustomer.setAge(customer.getClue().getAge());
            excelCustomer.setAddress(customer.getClue().getAddress());
            excelCustomer.setEmail(customer.getClue().getEmail());
            excelCustomer.setQq(customer.getClue().getQq());
            excelCustomer.setAppellation(ObjectUtils.isEmpty(customer.getClue().getAppellationOD())?"":customer.getClue().getAppellationOD().getTypeValue());
            excelCustomer.setJob(customer.getClue().getJob());
            excelCustomer.setDescription(customer.getDescription());
            excelCustomer.setActivityName(ObjectUtils.isEmpty(customer.getClue().getActivity())?"":customer.getClue().getActivity().getName());
            excelCustomer.setFullName(customer.getClue().getFullName());
            excelCustomer.setIntentionProductStr(ObjectUtils.isEmpty(customer.getClue().getIntentionProductOD())?"":customer.getClue().getIntentionProductOD().getName());
            excelCustomer.setNeedLoanStr(ObjectUtils.isEmpty(customer.getClue().getNeedLoanOD())?"":customer.getClue().getNeedLoanOD().getTypeValue());
            excelCustomer.setNextConnection(customer.getNextContactTime());
            excelCustomer.setSourceStr(ObjectUtils.isEmpty(customer.getClue().getSourceOD())?"":customer.getClue().getSourceOD().getTypeValue());
            excelCustomer.setOwnerUsername(ObjectUtils.isEmpty(customer.getClue().getOwnerUser())?"":customer.getClue().getOwnerUser().getName());
            excelCustomer.setYearIncome(customer.getClue().getYearIncome());
            excelCustomer.setWeixin(customer.getClue().getWeixin());
            excelCustomer.setPhone(customer.getClue().getPhone());
            excelCustomers.add(excelCustomer);
        });
        return excelCustomers;
    }

    @Override
    public TCustomer customerDetails(Integer id) {
        return customerMapper.selectDetailsByPrimaryKey(id);
    }
}
