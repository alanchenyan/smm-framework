package com.smm.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Alan Chen
 * @description 表情符工具类
 * @date 2020/12/8
 */
public class EmoticonTool {

    private EmoticonTool(){}

    /**
     * 将字符串里的表情符过滤掉
     * @param str
     * @return
     */
    public static String filterEmoticon(String str) {
        if (str.trim().isEmpty()) {
            return str;
        }
        String pattern = "[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]|[\uD83E\uDDF8]";
        String reStr = "";
        Pattern emoji = Pattern.compile(pattern);
        Matcher emojiMatcher = emoji.matcher(str);
        str = emojiMatcher.replaceAll(reStr);
        return str;
    }
}
