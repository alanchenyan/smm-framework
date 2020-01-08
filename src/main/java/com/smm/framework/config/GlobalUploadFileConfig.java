package com.smm.framework.config;

import com.smm.framework.util.FileUpLoadTool;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Alan Chen
 * @description 文件上传访问路径配置
 * @date 2020-01-08
 */
@Configuration
@EnableWebMvc
public class GlobalUploadFileConfig implements WebMvcConfigurer {

    protected String fileRedirectName(){
        return FileUpLoadTool.FILE_REDIRECT_NAME;
    }

    protected String uploadFilesDirectory(){
        return FileUpLoadTool.getDefalutUploadFilesDirectory();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/"+fileRedirectName()+"/**").addResourceLocations("file:" + uploadFilesDirectory());
    }

}
