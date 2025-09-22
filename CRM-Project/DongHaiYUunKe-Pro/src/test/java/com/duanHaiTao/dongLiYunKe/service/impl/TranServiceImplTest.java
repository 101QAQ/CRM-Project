package com.duanHaiTao.dongLiYunKe.service.impl;

import com.duanHaiTao.dongLiYunKe.repository.TTranmapper;
import com.duanHaiTao.dongLiYunKe.service.TranService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class TranServiceImplTest {

    @Autowired
    private TTranmapper tTranmapper;

    @Test
    void test() {
        log.info(tTranmapper.selectTranDetailsById(5).toString());

    }
}