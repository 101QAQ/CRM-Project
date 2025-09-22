package com.duanHaiTao.dongLiYunKe.handler;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TSystemInfo;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.repository.TSystemInfomapper;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

@Component
@Slf4j
public class DLYKSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private TSystemInfomapper systemInfoMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        String isNotLogin = request.getParameter("isNotLogin");
        TSystemInfo tSystemInfo = systemInfoMapper.selectByPrimaryKey(1);
        Calendar instance=Calendar.getInstance();
        log.info(isNotLogin);
        if (isNotLogin != null && isNotLogin.equals("true")) {
            instance.add(Calendar.DATE, 7);
        }else {
            instance.add(Calendar.SECOND,60*30);
        }
        PrintWriter out = response.getWriter();
        TUser user = (TUser) authentication.getPrincipal();
        R<String> ok;
        String token =JwtUtil.getToken(user,instance);
        if (user.getAuthoritiesStr().contains("admin")){
            ok=R.Ok(Code.SUCCESS,token);
        }else {
            if (tSystemInfo.getIsopen().equals("true")){
                ok=R.Ok(Code.SUCCESS,token);
            }else {
                ok=R.Err(Code.SYSTEM_MAINTAIN_EXCEPTION);
            }
        }
        String json = new ObjectMapper().writeValueAsString(ok);
        out.println(json);
    }
}
