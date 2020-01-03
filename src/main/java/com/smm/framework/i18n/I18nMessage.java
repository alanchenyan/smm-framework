package com.smm.framework.i18n;

/**
 * @author Alan Chen
 * @description I18nMessage
 * @date 2019/4/1
 */
public interface I18nMessage {

    /**
     * 国际化key
     * @return
     */
    String getI18nKey();

    /**
     * 国际化文件路径
     * @return
     */
    String getI18nResourcePath();

    /**
     * 获取描述信息
     * @return
     */
    default String getMessage(){
        String messageI18n = "";
        try {
            String value = I18nResourceHandleMapping.getInstance(getI18nResourcePath()).getValue(getI18nKey());
            messageI18n = new String(value.getBytes("ISO-8859-1"), "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messageI18n;
    }
}
