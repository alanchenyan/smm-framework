package com.smm.framework.authority.exception;

import com.alibaba.fastjson.JSONObject;
import com.smm.framework.i18n.I18nResource;
import com.smm.framework.i18n.I18nResourceFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alan Chen
 * @description 鉴权异常: 认证用户访问无权限资源时的异常
 * @date 2019-10-12
 */
@Component
public class GlobalAccessDeniedHandler implements AccessDeniedHandler {

    private I18nResource i18nResource = I18nResourceFactory.getI18nResource();

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e)  {
        responseException(httpServletRequest,httpServletResponse,e);
    }

    private void responseException(HttpServletRequest httpServletRequest,HttpServletResponse response,AccessDeniedException exception) {

        Map<String,Object> exceptionMap = new HashMap(3);

        exceptionMap.put("code",403);
        exceptionMap.put("msg", i18nResource.getValue("no_access",httpServletRequest));
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