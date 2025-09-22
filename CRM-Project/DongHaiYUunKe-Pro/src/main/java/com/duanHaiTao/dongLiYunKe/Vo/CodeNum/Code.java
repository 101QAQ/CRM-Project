package com.duanHaiTao.dongLiYunKe.Vo.CodeNum;

/**
 * 统一状态码枚举
 * 遵循HTTP语义，并扩展业务状态码
 */
public enum Code {
    // ===== 基础状态码（遵循HTTP状态码语义）=====

    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未授权访问"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    METHOD_NOT_ALLOWED(405, "请求方法不支持"),
    CONFLICT(409, "资源冲突"),
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),

    // ===== 业务状态码（6位数字，2位模块+2位功能+2位状态）=====
    // 用户模块（10开头）
    USER_LOGIN_FAIL(100001, "用户名或密码错误"),
    USER_DISABLED(100002, "账号已禁用"),
    USER_LOCKED(100003, "账号已锁定"),
    USER_NOT_EXIST(100004, "用户不存在"),
    USER_EXIST(100005, "用户已存在"),

    // 权限模块（11开头）
    PERMISSION_DENIED(110001, "权限不足"),
    TOKEN_INVALID(110002, "令牌无效"),
    TOKEN_EXPIRED(110003, "令牌过期"),
    TOKEN_ISNULL(110004,"令牌为空"),
    TOKEN_ERROR(110005,"令牌错误"),
    // 数据校验（12开头）
    DATA_VALIDATION_FAIL(120001, "数据校验失败"),
    DATA_DUPLICATE(120002, "数据重复"),
    DATA_NOT_EXIST(120003, "数据不存在"),
    DATA_ACCESS_EXCEPTION(120004,"数据库操作失败"),
    DATA_TRANSFORMATION_EXCEPTION(120005,"数据转换异常"),
    DATA_UP_EXCEPTION(120006,"数据上传异常"),

    // 第三方服务（20开头）
    THIRD_PARTY_ERROR(200001, "第三方服务异常"),
    API_CALL_LIMIT(200002, "API调用频率超限"),
    TOKEN_REFRESH(601,"token已刷新"),
    TOKEN_FAIL_REFRESH(602,"token刷新失败"),

    TOKEN_FAIL_PROCESSING(701,"token处理异常"),

    PHONE_EXIST_EXCEPTION(801,"号码已经存在"),

    CUSTOMER_EXIST_EXCEPTION(901,"客户存在异常"),

    PASSWORD_VALIDATE_EXCEPTION(1001,"密码验证异常"),

    SYSTEM_MAINTAIN_EXCEPTION(1201,"系统维护中");



    private final int code;
    private final String msg;

    Code(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * 根据code查找枚举
     */
    public static Code findByCode(int code) {
        for (Code value : values()) {
            if (value.code == code) {
                return value;
            }
        }
        return INTERNAL_SERVER_ERROR; // 默认返回服务器错误
    }
}
