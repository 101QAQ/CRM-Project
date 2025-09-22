package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TClueRemark;
import com.duanHaiTao.dongLiYunKe.query.QueryClueRemark;
import com.duanHaiTao.dongLiYunKe.service.ClueRemarkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clueRemark")
public class ClueRemarkController {

    @Autowired
    private ClueRemarkService clueRemarkService;

    @PostMapping("/add")
    public R<Object> add(@RequestBody QueryClueRemark queryClueRemark,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count=clueRemarkService.add(queryClueRemark,token);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/clueRemarkPage")
    public R<Object> activityPage(@RequestParam(value = "pageNum",required = false) Integer pageNum) throws JsonProcessingException {
        if (pageNum == null) {
            pageNum = 1;
        }
        PageInfo<Object> objectPageInfo = clueRemarkService.clueRemarkPage(pageNum);
        return R.Ok(Code.SUCCESS, objectPageInfo);
    }

    @GetMapping("/clueRemarkById")
    public R<Object> clueRemarkById(@RequestParam(value = "id") Integer id) throws JsonProcessingException {
        TClueRemark clueRemark=clueRemarkService.findClueRemarkById(id);
        return R.Ok(Code.SUCCESS,clueRemark);
    }

    @PutMapping("/edit")
    public R<Object> edit(@RequestBody QueryClueRemark queryClueRemark,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count =clueRemarkService.update(queryClueRemark,token);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete")
    public R<Object> delete(@RequestParam("id") Integer id) throws JsonProcessingException {
        return clueRemarkService.delete(id)>0? R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }
}
