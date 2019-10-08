//package com.smm.framework.authority.exceptionhandler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.smm.framework.response.ResponseResult;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * spring Security匿名用户访问无权限资源时的异常
// * @author -Huang
// * @create 2019-09-11 15:36
// */
//@Component
//public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request,
//                         HttpServletResponse response,
//                         AuthenticationException authException) throws IOException, ServletException {
//
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("application/json; charset=utf-8");
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseResult.info("授权过期，请重新登录。")));
//}
//}