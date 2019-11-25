package com.smm.framework.response;

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
        return new ResponseResult(200,"操作成功！",null);
    }

    public static ResponseResult info(String msg){
        return new ResponseResult(600,msg,null);
    }

    public static ResponseResult fail(Object data){
        return new ResponseResult(500,"服务器出错啦！",data);
    }

    public static ResponseResult fail(){
        return new ResponseResult(500,"服务器出错啦！",null);
    }

}
