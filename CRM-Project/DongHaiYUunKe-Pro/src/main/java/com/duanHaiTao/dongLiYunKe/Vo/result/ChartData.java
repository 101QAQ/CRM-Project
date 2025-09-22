package com.duanHaiTao.dongLiYunKe.Vo.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChartData {
    private String name;
    private Integer value;
}
