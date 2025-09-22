package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TCustomerRemark;
import com.duanHaiTao.dongLiYunKe.query.QueryCustomerRemark;
import com.duanHaiTao.dongLiYunKe.service.CustomerRemarkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customerRemark")
public class CustomerRemarkController {


    @Autowired
    private CustomerRemarkService customerRemarkService;

    @GetMapping("/remarkPage")
    public R<Object> getRemarkPage(@RequestParam("customerId") Integer customerId, @RequestParam(value = "pageNum",required = false) Integer pageNum){
        if (pageNum == null){
            pageNum=1;
        }
        PageInfo<Object> infor=customerRemarkService.customerRemarkPage(customerId,pageNum);
        return R.Ok(Code.SUCCESS,infor);
    }

    @GetMapping("/remarkDetails")
    public R<Object> getRemarkDetail(@RequestParam("id") Integer id){
        TCustomerRemark remark = customerRemarkService.findRemarkById(id);
        return R.Ok(Code.SUCCESS,remark);
    }

    @PutMapping("/edit")
    public R<Object> editRemark(@RequestBody TCustomerRemark remark,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count=customerRemarkService.edit(remark,token);
        return count>0? R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }


    @DeleteMapping("/delete")
    public R<Object> deleteRemark(@RequestParam("id") Integer id){
        int count=customerRemarkService.delete(id);
        return count>0? R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);

    }

    @PostMapping("/add")
    public R<Object> addRemark(@RequestBody TCustomerRemark customerRemark,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count=customerRemarkService.add(customerRemark,token);
        return count>0? R.Ok(Code.SUCCESS,count):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

}
