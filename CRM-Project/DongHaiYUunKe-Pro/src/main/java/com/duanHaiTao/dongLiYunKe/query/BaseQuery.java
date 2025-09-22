package com.duanHaiTao.dongLiYunKe.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseQuery {
    private String filterSql;
}
