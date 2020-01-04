package com.smm.framework.response;


import com.smm.framework.i18n.I18nMessage;

/**
 * @author AlanChen
 * @description
 * @date 2020/1/2
 */
public enum ResponseMessage implements I18nMessage {

    /**
     * 操作成功
     */
    SUCCESS("success"),

    /**
     * 服务器出错啦
     */
    SERVER_ERROR("server_error");

    /**
     * 国际化配置文件中定义的key
     */
    private String i18nKey;


    ResponseMessage(String i18nKey) {
        this.i18nKey = i18nKey;
    }


    @Override
    public String getI18nKey() {
        return i18nKey;
    }

    @Override
    public String getI18nResourcePath() {
        return "i18n/response";
    }
}
