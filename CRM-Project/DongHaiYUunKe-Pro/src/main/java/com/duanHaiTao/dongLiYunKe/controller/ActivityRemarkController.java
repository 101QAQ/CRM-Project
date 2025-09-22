package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TActivityRemark;
import com.duanHaiTao.dongLiYunKe.query.QueryRemark;
import com.duanHaiTao.dongLiYunKe.service.ActivityRemarkService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activityRemark")
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @PostMapping("/add")
    public R<Object> add(@RequestBody QueryRemark queryRemark, @RequestHeader("Authorization") String token) throws JsonProcessingException {
        int i = activityRemarkService.addRemark(queryRemark, token);
        return i>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/remarkList")
    public R<Object> getRemarkList(@RequestParam("activityId") Integer activityId){
        List<TActivityRemark> activityList=activityRemarkService.remarkListByActivityId(activityId);
        return R.Ok(Code.SUCCESS,activityList);
    }

    @GetMapping("/remarkPage")
    public R<Object> getRemarkPage(@RequestParam("activityId") Integer activityId,@RequestParam(value = "pageNum",required = false) Integer pageNum){
        if (pageNum == null){
            pageNum=1;
        }
        PageInfo<Object> infor=activityRemarkService.remarkPageByActivity(activityId,pageNum);
        return R.Ok(Code.SUCCESS,infor);
    }

    @GetMapping("/remark")
    public R<Object> getRemark(@RequestParam("id") Integer id){
        TActivityRemark tActivityRemark=activityRemarkService.getRemark(id);
        return R.Ok(Code.SUCCESS,tActivityRemark);
    }

    @PutMapping("/edit")
    public R<Object> edit(@RequestBody QueryRemark queryRemark, @RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count=activityRemarkService.updataRemark(queryRemark,token);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete")
    public R<Object> delete(@RequestParam("id") Integer id){
        int count=activityRemarkService.deleteRemark(id);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

}
