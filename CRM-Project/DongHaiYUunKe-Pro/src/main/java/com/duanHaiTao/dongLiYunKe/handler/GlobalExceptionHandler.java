package com.duanHaiTao.dongLiYunKe.handler;

import com.duanHaiTao.dongLiYunKe.Vo.CodeNum.Code;
import com.duanHaiTao.dongLiYunKe.Vo.result.R;
import com.duanHaiTao.dongLiYunKe.exceptions.CustomExistException;
import com.duanHaiTao.dongLiYunKe.exceptions.PasswordValidateException;
import com.duanHaiTao.dongLiYunKe.exceptions.PhoneIsExistException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataAccessException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public R<Object> exception(Exception e) {
        e.printStackTrace();
        return R.Err(Code.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataAccessException.class)
    public R<Object> exception(DataAccessException e) {
        e.printStackTrace();
        return R.Err(Code.DATA_ACCESS_EXCEPTION);
    }
    @ExceptionHandler(JsonProcessingException.class)
    public R<Object> jsonProcessingException(JsonProcessingException e) {
        e.printStackTrace();
        return R.Err(Code.DATA_TRANSFORMATION_EXCEPTION);
    }


    @ExceptionHandler(PhoneIsExistException.class)
    public R<Object> phoneIsExistException(PhoneIsExistException e) {
        e.printStackTrace();
        return R.Err(Code.PHONE_EXIST_EXCEPTION);
    }

    @ExceptionHandler(CustomExistException.class)
    public R<Object> customExistException(CustomExistException e) {
        e.printStackTrace();
        return R.Err(Code.CUSTOMER_EXIST_EXCEPTION);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public R<Object> accessDeniedException(AccessDeniedException e){
        e.printStackTrace();
        return R.Err(Code.PERMISSION_DENIED);
    }

    @ExceptionHandler(PasswordValidateException.class)
    public R<Object> passwordValidateException(PasswordValidateException e){
        e.printStackTrace();
        return R.Err(Code.PASSWORD_VALIDATE_EXCEPTION);
    }

}
