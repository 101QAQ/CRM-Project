package com.duanHaiTao.dongLiYunKe.controller;

import com.alibaba.excel.EasyExcel;
import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TCustomer;
import com.duanHaiTao.dongLiYunKe.query.ExcelCustomer;
import com.duanHaiTao.dongLiYunKe.query.QueryCustomer;
import com.duanHaiTao.dongLiYunKe.service.CustomerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/add")
    public R<Object> addCustomer(@RequestBody QueryCustomer queryCustomer, @RequestHeader("Authorization") String token) throws JsonProcessingException {
        int add = customerService.add(queryCustomer, token);
        return add > 1?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/customerPage")
    public R<Object> customerPage(@RequestParam(value = "pageNum",required = false) Integer pageNum){
        if (pageNum == null){
            pageNum = 1;
        }
        PageInfo<Object> info=customerService.customerPage(pageNum);
        return R.Ok(Code.SUCCESS,info);
    }

    @GetMapping("/exportExcel")
    public void exportExcel(HttpServletResponse response,@RequestParam(value = "ids",required = false) String ids) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("客户数据文档", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<ExcelCustomer> customers=customerService.exportExcel(ids);
        EasyExcel.write(response.getOutputStream(), ExcelCustomer.class)
                // 在 write 方法之后， 在 sheet方法之前都是设置WriteWorkbook的参数
                .sheet("模板")
                .doWrite(customers);
    }

    @GetMapping("/details")
    public R<Object> details(@RequestParam("id") Integer id){

        TCustomer customer=customerService.customerDetails(id);

        return R.Ok(Code.SUCCESS,customer);

    }


}
