package com.smm.framework.config.enumconvert;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Alan Chen
 * @description 枚举类型转换配置
 * @date 2020-02-06
 */
@Configuration
@EnableWebMvc
public class GlobalEnumconvertWebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new EnumConvertFactory());
    }
}
