package com.duanHaiTao.dongLiYunKe.Vo.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummaryStatistic {

    private String heldActivityAndSum;
    private Integer clueSum;
    private Integer customerSum;
    private String transactionSum;

}
