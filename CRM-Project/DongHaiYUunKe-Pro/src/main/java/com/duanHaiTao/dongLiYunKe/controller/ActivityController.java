package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TActivity;
import com.duanHaiTao.dongLiYunKe.query.QueryActivity;
import com.duanHaiTao.dongLiYunKe.service.ActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/activityPage")
    public R<Object> activityPage(@RequestParam(value = "pageNum",required = false) Integer pageNum,@RequestParam("condition") String condition) throws JsonProcessingException {
        if (pageNum == null) {
            pageNum = 1;
        }
        QueryActivity queryActivity;
        queryActivity=new ObjectMapper().readValue(condition, QueryActivity.class);
        PageInfo<Object> objectPageInfo = activityService.activityPage(pageNum,queryActivity);
        return R.Ok(Code.SUCCESS, objectPageInfo);
    }


    @GetMapping("/details")
    public R<Object> details(@RequestParam(value = "id") Integer id) {
        TActivity tActivity = activityService.activityDetails(id);
        return R.Ok(Code.SUCCESS, tActivity);
    }


    @PostMapping("/add")
    public R<Object> add(@RequestBody QueryActivity queryActivity,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count=activityService.addActivity(queryActivity,token);
        return count>=1?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }


    @PutMapping("/edit")
    public R<Object> edit(@RequestBody QueryActivity queryActivity,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count=activityService.updateActivity(queryActivity,token);
        return count>=1?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/activityList")
    public R<Object> activityList(){
        return R.Ok(Code.SUCCESS,activityService.findAll());
    }

    @DeleteMapping("/delete")
    public R<Object> delete(@RequestParam(value = "id") Integer id){
        return activityService.delete(id)>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/deleteBatch")
    public R<Integer> deleteBatch(@RequestParam(value = "ids") String ids){

        int i = activityService.deleteBatch(ids);

        return i>0?R.Ok(Code.SUCCESS,i):R.Err(Code.INTERNAL_SERVER_ERROR);

    }

}
