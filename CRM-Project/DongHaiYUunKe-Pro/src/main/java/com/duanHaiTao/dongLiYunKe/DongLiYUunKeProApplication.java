package com.duanHaiTao.dongLiYunKe;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@SpringBootApplication
@MapperScan("com.duanHaiTao.dongLiYunKe.repository")
public class DongLiYUunKeProApplication {
    public static void main(String[] args) {
        SpringApplication.run(DongLiYUunKeProApplication.class, args);
    }
}
