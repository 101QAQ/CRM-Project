package com.duanHaiTao.dongLiYunKe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 线索表
 * t_clue
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ExcelIgnoreUnannotated
public class TClue implements Serializable {
    /**
     * 主键，自动增长，线索ID
     */
    private Integer id;

    /**
     * 线索所属人ID
     */

    @ExcelProperty(value = "负责人")
    private Integer ownerId;

    private TUser ownerUser;

    /**
     * 活动ID
     */
    @ExcelProperty(value = "所属活动")
    private Integer activityId;

    private TActivity activity;

    /**
     * 姓名
     */
    @ExcelProperty("姓名")
    private String fullName;

    /**
     * 称呼
     */
    private Integer appellation;

    @ExcelProperty("称呼")
    private String appellationStr;
    private TDicValue appellationOD;

    /**
     * 手机号
     */
    @ExcelProperty("手机号")
    private String phone;

    /**
     * 微信号
     */
    @ExcelProperty("微信号")
    private String weixin;

    /**
     * QQ号
     */
    @ExcelProperty("QQ号")
    private String qq;

    /**
     * 邮箱
     */
    @ExcelProperty("邮箱")
    private String email;

    /**
     * 年龄
     */
    @ExcelProperty("年龄")
    private Integer age;

    /**
     * 职业
     */
    @ExcelProperty("职业")
    private String job;

    /**
     * 年收入
     */
    @ExcelProperty("年收入")

    private BigDecimal yearIncome;

    /**
     * 地址
     */
    @ExcelProperty("地址")
    private String address;

    /**
     * 是否需要贷款
     */
    private Integer needLoan;

    @ExcelProperty("是否贷款")
    private String needLoanStr;
    private TDicValue needLoanOD;

    /**
     * 意向状态
     */
    private Integer intentionState;

    @ExcelProperty("意向状态")
    private String intentionStateStr;
    private TDicValue intentionStateOD;

    /**
     * 意向产品
     */

    private Integer intentionProduct;

    @ExcelProperty("意向产品")
    private String intentionProductStr;

    private TProduct intentionProductOD;

    /**
     * 线索状态
     */
    private Integer state;

    @ExcelProperty("线索状态")
    private String stateStr;
    private TDicValue stateOD;

    /**
     * 线索来源
     */
    private Integer source;

    @ExcelProperty("线索来源")
    private String sourceStr;
    private TDicValue sourceOD;

    /**
     * 线索描述
     */
    @ExcelProperty("线索描述")
    private String description;

    /**
     * 下次联系时间
     */
    @ExcelProperty(value = "下次联系时间" ,format = "yyyy-MM-dd HH:mm:ss")
    private Date nextContactTime;

    /**
     * 创建时间
     */

    private Date createTime;

    /**
     * 创建人
     */

    private Integer createBy;

    private TUser createUser;

    /**
     * 编辑时间
     */

    private Date editTime;

    /**
     * 编辑人
     */

    private Integer editBy;

    private TUser editUser;

    private static final long serialVersionUID = 1L;
}