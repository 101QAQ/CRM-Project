package com.duanHaiTao.dongLiYunKe.aspect;

import com.duanHaiTao.dongLiYunKe.annotaion.DataFilter;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.query.BaseQuery;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class SqlDataAspect {

    @Pointcut("@annotation(com.duanHaiTao.dongLiYunKe.annotaion.DataFilter)")
    public void pointcut() {}


    @Around("pointcut()")
    public Object advice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        DataFilter annotation = signature.getMethod().getAnnotation(DataFilter.class);
        String tableAlias = annotation.tableAlias();
        String tableField = annotation.tableField();
        BaseQuery baseQuery = (BaseQuery) joinPoint.getArgs()[0];
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String Jwt;
        if (request.getRequestURI().equals("/customer/exportExcel")){
            Jwt=request.getParameter("Authorization");
        }else {
            Jwt=request.getHeader("Authorization");
        }
        TUser tUser = JwtUtil.parseToken(Jwt);
        if (!tUser.getAuthoritiesStr().contains("admin")) {
            baseQuery.setFilterSql("and " + tableAlias + "." + tableField + '=' + tUser.getId());
        }
        return joinPoint.proceed();
    }


}
