package com.smm.framework.i18n;

/**
 * @author Alan Chen
 * @description
 * @date 2020-01-04
 */
public class I18nResourceFactory {

    private final static I18nResource i18nResource = new I18nResource("i18n/response");

    private I18nResourceFactory(){
    }

    public static I18nResource getI18nResource(){
        return i18nResource;
    }
}