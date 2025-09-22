package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TSystemInfo;
import com.duanHaiTao.dongLiYunKe.service.SystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private SystemService systemService;

    @GetMapping("/infor")
    public R<Object> infor() {
        return R.Ok(Code.SUCCESS,systemService.getSystemInfo());
    }

    @PutMapping("/reset")
    public R<Object> reset(@RequestBody TSystemInfo systemInfo) {
        int count=systemService.reset(systemInfo);
        return count>0?R.Ok(Code.SUCCESS,count):R.Err(Code.INTERNAL_SERVER_ERROR);
    }






}
