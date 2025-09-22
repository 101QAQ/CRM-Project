package com.duanHaiTao.dongLiYunKe.repository;

import com.duanHaiTao.dongLiYunKe.service.TranHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TTranHistorymapperTest {

    @Autowired
    private TranHistoryService tranHistoryService;

    @Test
    public void test() {

        System.out.println(tranHistoryService.historyPage(1,5).getList());

    }


}