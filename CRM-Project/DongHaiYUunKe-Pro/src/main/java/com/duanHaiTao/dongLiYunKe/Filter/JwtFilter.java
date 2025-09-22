package com.duanHaiTao.dongLiYunKe.Filter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.duanHaiTao.dongLiYunKe.Common.JwtAuthenticationToken;
import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.model.TUser;
import com.duanHaiTao.dongLiYunKe.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String path = request.getRequestURI();
        if ("/login".equals(path)) {
            filterChain.doFilter(request, response);
            return;
        }
        String tokenStr;
        if ("/customer/exportExcel".equals(path)) {
            tokenStr=request.getParameter("Authorization");
        }else {
            tokenStr=request.getHeader("Authorization");
        }
        response.setContentType("application/json;charset=utf-8");

        if (tokenStr == null) {
            writeErrorResponse(response, R.Err(Code.TOKEN_ISNULL));
            return;
        }
        try {
            TUser tUser = JwtUtil.parseToken(tokenStr);
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(
                    tUser,
                    tUser.getAuthorities()
            );
            jwtAuthenticationToken.setAuthenticated(true);
            SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
            filterChain.doFilter(request, response);
        } catch (TokenExpiredException e) {
            writeErrorResponse(response, R.Err(Code.TOKEN_EXPIRED));
        } catch (SignatureVerificationException e) {
            writeErrorResponse(response, R.Err(Code.TOKEN_INVALID));
        }catch (JWTDecodeException e){
            writeErrorResponse(response, R.Err(Code.TOKEN_ERROR));
        } catch (Exception e) {
            writeErrorResponse(response, R.Err(Code.INTERNAL_SERVER_ERROR));
        }
    }

    private void writeErrorResponse(HttpServletResponse response, R<?> result)
            throws IOException {
        if (!response.isCommitted()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(result);
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
        }
    }
}
