package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TDicValue;
import com.duanHaiTao.dongLiYunKe.service.DicValueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dicValue")
public class DicValueController {
    @Autowired
    private DicValueService dicValueService;

    @GetMapping("/clueDicValue")
    public R<Object> clueDicValue(){
        List<String> typeCode=new ArrayList<>();
        typeCode.add("appellation");
        typeCode.add("needLoan");
        typeCode.add("intentionState");
        typeCode.add("clueState");
        typeCode.add("source");
        Map<String, List<TDicValue>> dicValueByDicCode = dicValueService.getDicValueByDicCode(typeCode);
        return R.Ok(Code.SUCCESS, dicValueByDicCode);
    }

    @GetMapping("/DicValue")
    public R<Object> clueDicValueByCode(@RequestParam("dicCode") String code){
        return R.Ok(Code.SUCCESS,dicValueService.getDicValueByDicCode(code));
    }

    @GetMapping("/dicValue/{id}")
    public R<Object> dicValueById(@PathVariable("id") Integer id){
        return R.Ok(Code.SUCCESS,dicValueService.findOne(id));
    }


    @GetMapping("/dicValueList")
    public R<Object> dicValueList(){
        return R.Ok(Code.SUCCESS,dicValueService.getDicValueList());
    }

    @GetMapping("/dicValuePage")
    public R<Object> getDicPage(@RequestParam(value = "pageNum",required = false) Integer pageNum, @RequestParam("condition") String condition) throws JsonProcessingException {
        if (pageNum==null){
            pageNum=1;
        }
        PageInfo<Object> info=dicValueService.dicValuePage(pageNum,condition);
        return R.Ok(Code.SUCCESS,info);

    }


    @PostMapping("/add")
    public R<Object> addDicValue(@RequestBody TDicValue dicValue){
        int count=dicValueService.add(dicValue);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);

    }
    @DeleteMapping("/delete")
    public R<Object> deleteDicValue(@RequestParam("id") Integer id){
        int count=dicValueService.delete(id);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);

    }

    @DeleteMapping("/deleteBatch")
    public R<Object> deleteBatch(@RequestParam("ids") String idStr){
        int count=dicValueService.deleteBath(idStr);
        return count>0?R.Ok(Code.SUCCESS,count):R.Err(Code.INTERNAL_SERVER_ERROR);
    }


    @PutMapping("/edit")
    public R<Object> editDicValue(@RequestBody TDicValue dicValue){
        int count=dicValueService.editDicValue(dicValue);
        return count>0?R.Ok(Code.SUCCESS,count):R.Err(Code.INTERNAL_SERVER_ERROR);
    }



}
