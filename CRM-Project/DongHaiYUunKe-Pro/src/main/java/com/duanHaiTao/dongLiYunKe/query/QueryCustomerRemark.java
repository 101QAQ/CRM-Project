package com.duanHaiTao.dongLiYunKe.query;

import com.duanHaiTao.dongLiYunKe.model.TDicValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryCustomerRemark {

    private Integer id;

    private Integer customerId;

    private Integer noteWay;

    private String noteContent;

}
