package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.service.DicTypeService;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dicType")
public class DicTypeController {

    @Autowired
    private DicTypeService dicTypeService;

    @GetMapping("/dicTypeList")
    public R<Object> dicTypeList(){
        return R.Ok(Code.SUCCESS,dicTypeService.getDicTypeList());
    }

}
