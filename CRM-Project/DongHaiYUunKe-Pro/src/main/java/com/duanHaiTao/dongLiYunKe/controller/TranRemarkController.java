package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TTranRemark;
import com.duanHaiTao.dongLiYunKe.service.TranRemarkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tranRemark")
public class TranRemarkController {


    @Autowired
    private TranRemarkService tranRemarkService;

    @GetMapping("/tranRemakePage")
    public R<Object> tranPage(@RequestParam(value = "pageNum",required = false) Integer pageNum, @RequestParam("tranId") Integer tranId){
        if (pageNum == null) {
            pageNum = 1;
        }
        PageInfo<Object> info=tranRemarkService.remarkPage(pageNum,tranId);
        return R.Ok(Code.SUCCESS,info);
    }


    @PostMapping("/add")
    private R<Object> add(@RequestBody TTranRemark tTranRemark,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count=tranRemarkService.add(tTranRemark,token);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.SUCCESS);
    }

}
