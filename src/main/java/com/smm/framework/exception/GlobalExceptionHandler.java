package com.smm.framework.exception;

import com.smm.framework.response.ResponseResult;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 * @author -Huang
 * @create 2019-09-05 8:58
 */
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
        return ResponseResult.info(stringBuffer.toString());
    }

    /**
     * 拦截捕捉自定义异常 CommonException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public ResponseResult commonExceptionHandler(ServiceException ex) {
        return ResponseResult.info(ex.getMessage());
    }

}
