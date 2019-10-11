package com.smm.framework.config;

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
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Alan Chen
 * @description Controller返回参数全局包装成ResponseResult对象
 * @date 2019-10-11
 */
@EnableWebMvc
@Configuration
public class GlobalReturnConfig {

    @RestControllerAdvice
    static class ResultResponseAdvice implements ResponseBodyAdvice<Object> {
        @Override
        public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
            return true;
        }

        @Override
        public Object beforeBodyWrite(Object returnObj, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

            if(returnObj==null){ // 返回值为void
                return ResponseResult.success();
            }

            //全局异常会拦截统一封装成ResponseResult对象，因此不需要再包装了
            if(returnObj instanceof ResponseResult){
                return returnObj;
            }

            if(returnObj instanceof String){
                throw new ServiceException("Controller层暂时不支持返回String类型参数。如需返回String参数，请用ResponseResult.success(obj)方式返回");
            }

            return  ResponseResult.success(returnObj);

        }
    }
}