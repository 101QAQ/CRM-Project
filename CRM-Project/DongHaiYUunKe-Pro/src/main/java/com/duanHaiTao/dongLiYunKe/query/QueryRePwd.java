package com.duanHaiTao.dongLiYunKe.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryRePwd {

    private String oldPwd;
    private String newPwd;

}
