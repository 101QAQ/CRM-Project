package com.duanHaiTao.dongLiYunKe.query;

import lombok.Data;

import javax.management.Query;

@Data
public class QueryUser extends BaseQuery {
    private Integer id;

    private String loginAct;

    private String loginPwd;

    private String name;

    private String phone;

    private String email;

    private Integer accountNoExpired;

    private Integer credentialsNoExpired;

    private Integer accountNoLocked;

    private Integer accountEnabled;
}
