package com.duanHaiTao.dongLiYunKe.controller;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.service.TranHistoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tranHistory")
public class TranHistoryController {

    @Autowired
    private TranHistoryService tranHistoryService;

    @GetMapping("/historyPage")
    public R<Object> tranPage(@RequestParam(value = "pageNum",required = false) Integer pageNum,@RequestParam("tranId") Integer tranId) {
        if (pageNum == null) {
            pageNum = 1;
        }
        PageInfo<Object> info=tranHistoryService.historyPage(pageNum,tranId);
        return R.Ok(Code.SUCCESS,info);
    }

}
