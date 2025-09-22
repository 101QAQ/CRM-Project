package com.duanHaiTao.dongLiYunKe;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
class DongLiYUunKeProApplicationTests {

    @Test
    void contextLoads() {
        String format = new SimpleDateFormat("yyyy-MM").format(new Date());
    }

}
