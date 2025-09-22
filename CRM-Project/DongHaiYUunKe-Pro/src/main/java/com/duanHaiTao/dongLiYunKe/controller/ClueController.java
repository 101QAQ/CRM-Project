package com.duanHaiTao.dongLiYunKe.controller;

import com.alibaba.excel.EasyExcel;
import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TClue;
import com.duanHaiTao.dongLiYunKe.service.ClueService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/clue")
@Slf4j
public class ClueController {

    @Autowired
    private ClueService clueService;


    @GetMapping("/cluePage")
    public R<Object> cluePage(@RequestParam(value = "pageNum",required = false) Integer pageNum,@RequestParam("condition") String condition) throws JsonProcessingException {
        if (pageNum == null) {
            pageNum = 1;
        }
        PageInfo<Object> objectPageInfo = clueService.cluePage(pageNum,condition);
        return R.Ok(Code.SUCCESS, objectPageInfo);
    }

    @PostMapping("/importClue")
    public R<Object> importClue(MultipartFile file,@RequestHeader("Authorization") String token) throws IOException {
        try {
            clueService.upFile(file,token);
        } catch (IOException e) {
            return R.Err(Code.DATA_UP_EXCEPTION);
        }
        return R.Ok(Code.SUCCESS);
    }

    @GetMapping("/downModel")
    public void downModel(HttpServletResponse response) throws IOException {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("线索模版", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            EasyExcel.write(response.getOutputStream(), TClue.class)
                .sheet("员工信息")
                .doWrite(clueService.downModel());
    }


    @PostMapping("/add")
    public R<Object> add(@RequestBody TClue clue,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count =clueService.add(clue,token);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getClue")
    public R<Object> getClue(@RequestParam("id") Integer id) {
        return R.Ok(Code.SUCCESS,clueService.findClueById(id));
    }

    @PutMapping("/edit")
    public R<Object> edit(@RequestBody TClue clue,@RequestHeader("Authorization") String token) throws JsonProcessingException {
        int count=clueService.edit(clue,token);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete")
    public R<Object> delete(@RequestParam("id") Integer id) {
        int count=clueService.delete(id);
        return count>0?R.Ok(Code.SUCCESS):R.Err(Code.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/deleteBatch")
    public R<Object> deleteBatch(@RequestParam("ids") String idStr) {
        List<String> list = Arrays.asList(idStr.split(","));
        int count=clueService.deleteBatch(list);
        return count>0?R.Ok(Code.SUCCESS,count):R.Err(Code.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/clueDetails")
    public R<Object> getClueDetail(@RequestParam("id") Integer id) {
        TClue clue=clueService.findClueDetails(id);
        return R.Ok(Code.SUCCESS,clue);
    }


    @GetMapping("/checkPhone")
    public R<Object> checkPhone(@RequestParam("phone") String phone) {
        Boolean isNotExist=clueService.checkPhone(phone);
        return isNotExist?R.Ok(Code.SUCCESS):R.Err(Code.PHONE_EXIST_EXCEPTION);
    }

}
