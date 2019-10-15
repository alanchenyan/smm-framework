package com.smm.framework.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alan Chen
 * @description Swagger文档配置
 * @date 2019/5/15
 */

public class GlobalSwaggerConfig implements WebMvcConfigurer {


    private final int DOCKET_MAX_SIZE =10;

    private List<Docket> docketList;

    /**
     * 是否开启Swagger
     * @return
     */
    public boolean swaggerEnable(){
        return true;
    }

    /**
     * 有时候API会分为多个模块，因此需要对API进行分组显示，此时可以重写configureSwaggerApiInfo（）方法，返回多个你需要的ApiInfo
     * @return
     */
    protected List<SwaggerApiInfo> configureSwaggerApiInfo(){
        List<SwaggerApiInfo> swaggerApiInfos = new ArrayList<>();
        SwaggerApiInfo swaggerApiInfo = new SwaggerApiInfo("API接口文档","","V1.0");
        swaggerApiInfos.add(swaggerApiInfo);
        return swaggerApiInfos;
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(new String[]{"/**"}).addResourceLocations(new String[]{"classpath:/static/"});
        registry.addResourceHandler(new String[]{"swagger-ui.html"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/"});
        registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"});
    }

    private void initDockets(){

        docketList = new ArrayList<>(DOCKET_MAX_SIZE);

        List<SwaggerApiInfo> swaggerApiInfoList = configureSwaggerApiInfo();

        if(swaggerApiInfoList.size()>DOCKET_MAX_SIZE){
            throw new RuntimeException("smm框架Swagger最多只支持配置"+DOCKET_MAX_SIZE+"个Docket");
        }

        initConfigureDockets(swaggerApiInfoList);

        initDisableDockets(swaggerApiInfoList.size());
    }

    private void initDisableDockets(int configureDocketsSize){
        for(int i=configureDocketsSize-1;i<DOCKET_MAX_SIZE;i++){
            Docket disableDocket= (new Docket(DocumentationType.SWAGGER_2)).groupName(i+"").enable(false);
            docketList.add(disableDocket);
        }
    }



    private void initConfigureDockets(List<SwaggerApiInfo> swaggerApiInfoList){
        for(SwaggerApiInfo swaggerApiInfo : swaggerApiInfoList){

            ApiInfo apiInfo = (new ApiInfoBuilder()).title(swaggerApiInfo.getGroupName()).description("").termsOfServiceUrl("").version(swaggerApiInfo.getVersion()).build();

            Docket docket = (new Docket(DocumentationType.SWAGGER_2)).groupName(swaggerApiInfo.getGroupName()).enable(swaggerEnable()).apiInfo(apiInfo).select()
                    .apis(RequestHandlerSelectors.basePackage(swaggerApiInfo.getBasePackage())).paths(PathSelectors.any()).build();

            docketList.add(docket);
        }
    }


    @Bean
    public Docket createRestApi_0() {
        if(docketList == null){
            initDockets();
        }
        return docketList.get(0);
    }

    @Bean
    public Docket createRestApi_1() {
        return docketList.get(1);
    }

    @Bean
    public Docket createRestApi_2() {
        return docketList.get(2);
    }

    @Bean
    public Docket createRestApi_3() {
        return docketList.get(3);
    }

    @Bean
    public Docket createRestApi_4() {
        return docketList.get(4);
    }

    @Bean
    public Docket createRestApi_5() {
        return docketList.get(5);
    }

    @Bean
    public Docket createRestApi_6() {
        return docketList.get(6);
    }

    @Bean
    public Docket createRestApi_7() {
        return docketList.get(7);
    }

    @Bean
    public Docket createRestApi_8() {
        return docketList.get(8);
    }

    @Bean
    public Docket createRestApi_9() {
        return docketList.get(9);
    }

}
