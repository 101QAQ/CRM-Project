package com.duanHaiTao.dongLiYunKe.query;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
@Data
public class ExcelCustomer {

    @ExcelProperty("所属人")
    private String ownerUsername;
    @ExcelProperty("所属活动")
    private String activityName;
    @ExcelProperty("客户姓名")
    private String fullName;
    @ExcelProperty("客户称呼")
    private String appellation;
    @ExcelProperty("客户手机")
    private String phone;
    @ExcelProperty("客户邮箱")
    private String email;
    @ExcelProperty("客户地址")
    private String address;
    @ExcelProperty("客户微信")
    private String weixin;
    @ExcelProperty("客户qq")
    private String qq;
    @ExcelProperty("客户年龄")
    private Integer age;

    @ExcelProperty("客户职业")
    private String job;
    @ExcelProperty("客户年收入")
    private BigDecimal yearIncome;
    @ExcelProperty("是否贷款")
    private String needLoanStr;

    @ExcelProperty("意向产品")
    private String intentionProductStr;

    @ExcelProperty("客户来源")
    private String sourceStr;

    @ExcelProperty("客户描述")
    private String description;

    @ExcelProperty("下次联系时间")
    private Date nextConnection;

}
