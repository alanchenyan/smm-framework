package com.smm.framework.exception;

import com.smm.framework.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Alan Chen
 * @description 全局异常处理
 * @date 2019/5/15
 */
@Slf4j
@ControllerAdvice
@Component
public class GlobalExceptionHandler {
    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseResult errorHandler(Exception ex) {
        log.error("系统异常Exception："+ex.getMessage());
        ex.printStackTrace();
        return ResponseResult.fail(ex.getMessage());
    }

    /**
     * validator校验失败信息处理
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResult validationHandler(MethodArgumentNotValidException exception) {
        StringBuffer stringBuffer = new StringBuffer();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            stringBuffer.append(error.getDefaultMessage()).append(";");
        }
        log.error("数据校验异常MethodArgumentNotValidException："+stringBuffer.toString());
        exception.printStackTrace();
        return ResponseResult.fail(stringBuffer.toString());
    }

    /**
     * 拦截捕捉业务异常 ServiceException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public ResponseResult commonExceptionHandler(ServiceException ex) {
        log.error("业务异常ServiceException："+ex.getMessage());
        ex.printStackTrace();
        return ResponseResult.info(ex.getMessage());
    }

}
