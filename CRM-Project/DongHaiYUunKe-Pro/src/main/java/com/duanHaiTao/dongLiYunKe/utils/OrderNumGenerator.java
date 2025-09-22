package com.duanHaiTao.dongLiYunKe.utils;

import java.text.SimpleDateFormat;

import java.util.Date;

public class OrderNumGenerator {

    public static String generateOrderNumber() {
        // 格式化日期时间（精确到毫秒）
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSSS");
        return simpleDateFormat.format(new Date());
    }

}
