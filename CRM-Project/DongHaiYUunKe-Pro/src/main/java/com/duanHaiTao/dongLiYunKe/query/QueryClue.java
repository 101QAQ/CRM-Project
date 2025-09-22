package com.duanHaiTao.dongLiYunKe.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryClue {
    private Integer id;
    private Integer ownerId;
    private Integer activityId;
    private String fullName;
    private String phone;
}
