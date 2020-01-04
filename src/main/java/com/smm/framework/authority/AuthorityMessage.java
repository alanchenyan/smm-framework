package com.smm.framework.authority;


import com.smm.framework.i18n.I18nMessage;

/**
 * @author AlanChen
 * @description
 * @date 2020/1/2
 */
public enum AuthorityMessage implements I18nMessage {

    /**
     * 无权限访问
     */
    NO_ACCESS("no_access"),

    /**
     * 授权失败
     */
    AUTHENTICATION_FAILED("authentication_failed");

    /**
     * 国际化配置文件中定义的key
     */
    private String i18nKey;


    AuthorityMessage(String i18nKey) {
        this.i18nKey = i18nKey;
    }


    @Override
    public String getI18nKey() {
        return i18nKey;
    }

    @Override
    public String getI18nResourcePath() {
        return "i18n/authority";
    }
}
