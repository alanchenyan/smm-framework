package com.smm.framework.exception;

/**
 * @author Alan Chen
 * @description 业务异常
 * @date 2019/5/15
 */
public class ServiceException extends RuntimeException {

    private String[] placeholder;

    public ServiceException() {
    }

    public ServiceException(String message,String... placeholder) {
        super(message);
        this.placeholder = placeholder;
    }


    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public String[] getPlaceholder() {
        return placeholder;
    }
}
