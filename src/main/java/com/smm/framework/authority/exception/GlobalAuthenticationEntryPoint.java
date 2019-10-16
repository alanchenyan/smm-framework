package com.smm.framework.authority.exception;

import com.alibaba.fastjson.JSONObject;
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
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        responseException(httpServletResponse);
    }

    private void responseException(HttpServletResponse response) {

        Map<String,Object> exceptionMap = new HashMap(3);

        exceptionMap.put("code",403);
        exceptionMap.put("msg","授权认证失败");
        exceptionMap.put("data","");

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
