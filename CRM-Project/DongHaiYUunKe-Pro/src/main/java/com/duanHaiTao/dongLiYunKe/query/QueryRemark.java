package com.duanHaiTao.dongLiYunKe.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryRemark {

    private Integer id;

    private Integer activityId;

    private String noteContent;
}
