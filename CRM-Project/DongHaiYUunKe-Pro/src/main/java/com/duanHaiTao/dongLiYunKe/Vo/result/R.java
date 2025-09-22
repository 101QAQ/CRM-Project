package com.duanHaiTao.dongLiYunKe.Vo.result;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class R <T>{
    private Integer code;
    private String msg;
    private T data;


    public static <T>R<T> Ok(Code code){
        return R.<T>builder().msg(code.getMsg()).code(code.getCode()).build();
    }

    public static <T> R<T> Ok(Code code,T data){
        return R.<T>builder().msg(code.getMsg()).code(code.getCode()).data(data).build();
    }

    public static <T> R<T> Err(Code code){
        return R.<T>builder().msg(code.getMsg()).code(code.getCode()).build();
    }
}
