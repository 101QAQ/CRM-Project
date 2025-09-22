package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TTran;
import com.duanHaiTao.dongLiYunKe.service.TranService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tran")
public class TranController {

    @Autowired
    private TranService tranService;


    @GetMapping("/tranPage")
    public R<Object> tranPage(@RequestParam(value = "pageNum",required = false) Integer pageNum,@RequestParam("condition") String condition) throws JsonProcessingException {
        if (pageNum == null) {
            pageNum = 1;
        }
        PageInfo<Object> info=tranService.tranPage(pageNum,condition);
        return R.Ok(Code.SUCCESS,info);
    }

    @PostMapping("/add")
    public R<Object> addTran(@RequestBody TTran tran, @RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count=tranService.add(tran, token);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);

    }

    @GetMapping("/details")
    public R<Object> details(@RequestParam("id") Integer id) throws JsonProcessingException {
        TTran tran=tranService.details(id);
        return R.Ok(Code.SUCCESS,tran);
    }

    @PutMapping("/editStage")
    public R<Object> editStage(@RequestBody TTran tran,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count=tranService.editStage(tran,token);
        return R.Ok(Code.SUCCESS,count);
    }


}
