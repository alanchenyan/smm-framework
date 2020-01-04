package com.smm.framework.authority.exception;

import com.alibaba.fastjson.JSONObject;
import com.smm.framework.i18n.I18nResource;
import com.smm.framework.i18n.I18nResourceFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alan Chen
 * @description 认证异常: 用户在认证过程中的异常
 * @date 2019-10-12
 */
@Component
public class GlobalAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private I18nResource i18nResource = I18nResourceFactory.getI18nResource();

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)  {
        responseException(httpServletResponse,e);
    }

    private void responseException(HttpServletResponse response,AuthenticationException exception) {

        Map<String,Object> exceptionMap = new HashMap(3);

        exceptionMap.put("code",403);
        exceptionMap.put("msg", i18nResource.getValue("authentication_failed"));
        exceptionMap.put("data",exception.getMessage());

        JSONObject responseJsonObject = new JSONObject(exceptionMap);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(responseJsonObject.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
