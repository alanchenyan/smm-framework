package com.smm.framework.response;

import lombok.Data;

/**
 * @Author -Huang
 * @create 2019/9/2 17:21
 */
@Data
public class ResponseResult {
    private int code;
    private String msg;
    private Object data;

    private ResponseResult(int code, String msg) {
        this(code,msg,null);
    }

    private ResponseResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResponseResult success(Object data){
        return new ResponseResult(200,"操作成功！",data);
    }

    public static ResponseResult success(){
        return new ResponseResult(200,"操作成功！");
    }

    public static ResponseResult info(String msg){
        return new ResponseResult(600,msg);
    }

    public static ResponseResult fail(Object data){
        return new ResponseResult(500,"服务器出错啦！",data);
    }

    public static ResponseResult fail(){
        return new ResponseResult(500,"服务器出错啦！");
    }

}
