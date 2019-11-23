package com.smm.framework.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.smm.framework.exception.ServiceException;
import com.smm.framework.response.ResponseResult;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.List;

/**
 * @author Alan Chen
 * @description Controller返回参数全局包装成ResponseResult对象
 * @date 2019-10-11
 */
/**
 * @author Alan Chen
 * @description Controller返回参数全局包装成ResponseResult对象
 *              使用是一般需要指定basePackages，@RestControllerAdvice(basePackages = {"com.netx.web.controller"})
 *              只拦截controller包下的类；否则swagger也会拦截影响swagger正常使用
 * @date 2019-10-11
 */
@EnableWebMvc
@Configuration
@RestControllerAdvice
public class GlobalReturnConfig  implements ResponseBodyAdvice<Object> , WebMvcConfigurer {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object returnObj, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        // 返回值为void
        if(returnObj==null){
            return ResponseResult.success();
        }

        //全局异常会拦截统一封装成ResponseResult对象，因此不需要再包装了
        if(returnObj instanceof ResponseResult){
            return returnObj;
        }

        return  ResponseResult.success(returnObj);

    }

    /**
     * 解决不能返回单个字符的问题
     * @param converters
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new FastJsonHttpMessageConverter());
    }
}