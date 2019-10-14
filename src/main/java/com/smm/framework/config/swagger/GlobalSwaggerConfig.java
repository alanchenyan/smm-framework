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

public abstract class GlobalSwaggerConfig implements WebMvcConfigurer {


    private List<Docket> docketList;

    /**
     * 是否开启Swagger
     * @return
     */
    public boolean swaggerEnable(){
        return true;
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(new String[]{"/**"}).addResourceLocations(new String[]{"classpath:/static/"});
        registry.addResourceHandler(new String[]{"swagger-ui.html"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/"});
        registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"});
    }

    private void initDockets(){

        List<SwaggerApiInfo> swaggerApiInfoList = configureSwaggerApiInfo();

        docketList = new ArrayList<>(swaggerApiInfoList.size());

        for(SwaggerApiInfo swaggerApiInfo : swaggerApiInfoList){

            ApiInfo apiInfo = (new ApiInfoBuilder()).title(swaggerApiInfo.getGroupName()).description("").termsOfServiceUrl("").version(swaggerApiInfo.getVersion()).build();

            Docket docket = (new Docket(DocumentationType.SWAGGER_2)).groupName(swaggerApiInfo.getGroupName()).enable(swaggerEnable()).apiInfo(apiInfo).select()
                    .apis(RequestHandlerSelectors.basePackage(swaggerApiInfo.getBasePackage())).paths(PathSelectors.any()).build();

            docketList.add(docket);
        }

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


    @Bean
    public Docket createRestApi_1() {

        if(docketList == null){
            initDockets();
        }

        if(docketList.size()>=1){
            return docketList.get(0);
        }else{
            return (new Docket(DocumentationType.SWAGGER_2)).groupName("1").enable(false);
        }
    }

    @Bean
    public Docket createRestApi_2() {
        if(docketList.size()>=2){
            return docketList.get(1);
        }else{
            return (new Docket(DocumentationType.SWAGGER_2)).groupName("2").enable(false);
        }
    }

    @Bean
    public Docket createRestApi_3() {
        if(docketList.size()>=3){
            return docketList.get(2);
        }else{
            return (new Docket(DocumentationType.SWAGGER_2)).groupName("3").enable(false);
        }
    }

    @Bean
    public Docket createRestApi_4() {
        if(docketList.size()>=4){
            return docketList.get(3);
        }else{
            return (new Docket(DocumentationType.SWAGGER_2)).groupName("4").enable(false);
        }
    }

    @Bean
    public Docket createRestApi_5() {
        if(docketList.size()>=5){
            return docketList.get(4);
        }else{
            return (new Docket(DocumentationType.SWAGGER_2)).groupName("5").enable(false);
        }
    }

    @Bean
    public Docket createRestApi_6() {
        if(docketList.size()>=6){
            return docketList.get(5);
        }else{
            return (new Docket(DocumentationType.SWAGGER_2)).groupName("6").enable(false);
        }
    }

    @Bean
    public Docket createRestApi_7() {
        if(docketList.size()>=7){
            return docketList.get(6);
        }else{
            return (new Docket(DocumentationType.SWAGGER_2)).groupName("7").enable(false);
        }
    }

    @Bean
    public Docket createRestApi_8() {
        if(docketList.size()>=8){
            return docketList.get(7);
        }else{
            return (new Docket(DocumentationType.SWAGGER_2)).groupName("8").enable(false);
        }
    }

    @Bean
    public Docket createRestApi_9() {
        if(docketList.size()>=9){
            return docketList.get(8);
        }else{
            return (new Docket(DocumentationType.SWAGGER_2)).groupName("9").enable(false);
        }
    }

    @Bean
    public Docket createRestApi_10() {
        if(docketList.size()>=10){
            return docketList.get(9);
        }else{
            return (new Docket(DocumentationType.SWAGGER_2)).groupName("10").enable(false);
        }
    }

}
