package com.smm.framework.exception;

import com.smm.framework.i18n.I18nResource;
import com.smm.framework.response.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
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
     * 是否开启Validator国际化功能
     * @return
     */
    protected boolean enableValidationi18n(){
        return false;
    }

    /**
     * 国际化文件地址
     * @return
     */
    protected String validationMessageSourcePath(){
        return "i18n/validation/validation";
    }


    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResponseResult errorHandler(Exception ex) {
        ex.printStackTrace();
        return ResponseResult.fail(ex.getMessage());
    }

    /**
     * validator校验失败信息处理
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public ResponseResult bindExceptionHandler(BindException exception) {
        exception.printStackTrace();
        return doValidationException(exception.getBindingResult());
    }

    /**
     * validator校验失败信息处理
     * @param exception
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResult validationHandler(MethodArgumentNotValidException exception) {
        exception.printStackTrace();
        return doValidationException(exception.getBindingResult());
    }

    /**
     * 拦截捕捉业务异常 ServiceException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public ResponseResult commonExceptionHandler(ServiceException ex) {
        ex.printStackTrace();
        return ResponseResult.info(ex.getMessage());
    }

    private ResponseResult doValidationException(BindingResult bindingResult){
        StringBuffer stringBuffer = new StringBuffer();

        if(enableValidationi18n()){
            I18nResource resource = new I18nResource(validationMessageSourcePath());

            for (FieldError error : bindingResult.getFieldErrors()) {
                String messageKey = error.getDefaultMessage();
                try{
                    String message = resource.getValue(messageKey);
                    stringBuffer.append(message).append(";");
                }catch (Exception e){
                    stringBuffer.append(messageKey).append(";");
                }
            }
        }else{
            for (FieldError error : bindingResult.getFieldErrors()) {
                stringBuffer.append(error.getDefaultMessage()).append(";");
            }
        }
        return ResponseResult.fail(stringBuffer.toString());
    }

}
