package com.duanHaiTao.dongLiYunKe.query;

import lombok.Data;

import java.util.Date;

@Data
public class QueryCustomer extends BaseQuery {

    private Integer id;

    private Integer clueId;

    private Integer product;

    private String description;

    private Date nextContactTime;


}
