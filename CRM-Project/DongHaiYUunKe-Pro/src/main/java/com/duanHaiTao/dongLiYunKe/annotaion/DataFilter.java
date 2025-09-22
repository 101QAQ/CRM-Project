package com.duanHaiTao.dongLiYunKe.annotaion;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DataFilter {

    String tableAlias() default "";


    String tableField() default "";

}
