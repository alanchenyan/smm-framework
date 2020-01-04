package com.smm.framework.response;

import com.smm.framework.i18n.I18nResource;
import com.smm.framework.i18n.I18nResourceFactory;
import lombok.Data;


/**
 * @author Alan Chen
 * @description 响应前端统一格式
 * @date 2019/5/15
 */
@Data
public class ResponseResult {
    private int code;
    private String msg;
    private Object data;

    //private static I18nResource i18nResource = I18nResourceFactory.getI18nResource();

    private static I18nResource i18nResource;

    private ResponseResult(){}

    private ResponseResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseResult success(Object data){
        return new ResponseResult(200,"",data);
    }

    public static ResponseResult success(){
        return new ResponseResult(200,i18nResource.getValue("success"),"200");
    }

    public static ResponseResult info(String msg){
        return new ResponseResult(600,msg,"600");
    }

    public static ResponseResult fail(Object data){
        if(i18nResource == null){
            i18nResource = new I18nResource("i18n/server_message");
        }
        return new ResponseResult(500,i18nResource.getValue("server_error"),data);
    }

    public static ResponseResult fail(){
        return new ResponseResult(500,i18nResource.getValue("server_error"),"500");
    }

}
