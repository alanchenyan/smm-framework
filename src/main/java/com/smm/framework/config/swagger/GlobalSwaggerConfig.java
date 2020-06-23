package com.smm.framework.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alan Chen
 * @description Swagger文档配置
 * @date 2019/5/15
 */
@Configuration
@EnableSwagger2
public class GlobalSwaggerConfig implements WebMvcConfigurer {

    private final int DOCKET_MAX_SIZE = 15;

    private List<Docket> docketList;

    private static List<SwaggerApiInfo> swaggerApiInfosPro = new ArrayList();

    public static void addSwaggerApiInfos(SwaggerApiInfo swaggerApiInfo) {

        swaggerApiInfosPro.add(swaggerApiInfo);
    }

    public GlobalSwaggerConfig() {

    }


    public boolean swaggerEnable() {

        return true;
    }

    protected List<SwaggerApiInfo> configureSwaggerApiInfo() {

        List<SwaggerApiInfo> swaggerApiInfos = new ArrayList();
        SwaggerApiInfo swaggerApiInfo = new SwaggerApiInfo("API接口文档", "default", "V1.0");
        swaggerApiInfos.add(swaggerApiInfo);
        return swaggerApiInfos;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler(new String[]{"/**"}).addResourceLocations(new String[]{"classpath:/static/"});
        registry.addResourceHandler(new String[]{"swagger-ui.html"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/"});
        registry.addResourceHandler(new String[]{"/webjars/**"}).addResourceLocations(new String[]{"classpath:/META-INF/resources/webjars/"});
    }

    private void initDockets() {

        this.docketList = new ArrayList(DOCKET_MAX_SIZE);

        List<SwaggerApiInfo> swaggerApiInfos = this.configureSwaggerApiInfo();
        swaggerApiInfos.addAll(swaggerApiInfosPro);

        List<SwaggerApiInfo> swaggerApiInfoList = swaggerApiInfos;
        if (swaggerApiInfoList.size() > DOCKET_MAX_SIZE) {
            throw new RuntimeException("smm框架Swagger最多只支持配置15个Docket");
        } else {
            this.initConfigureDockets(swaggerApiInfoList);
            this.initDisableDockets(swaggerApiInfoList.size());
        }
    }

    private void initDisableDockets(int configureDocketsSize) {

        for (int i = configureDocketsSize - 1; i < DOCKET_MAX_SIZE; ++i) {
            Docket disableDocket = (new Docket(DocumentationType.SWAGGER_2)).groupName(i + "").enable(false);
            this.docketList.add(disableDocket);
        }

    }

    private void initConfigureDockets(List<SwaggerApiInfo> swaggerApiInfoList) {

        Iterator var2 = swaggerApiInfoList.iterator();

        while (var2.hasNext()) {
            SwaggerApiInfo swaggerApiInfo = (SwaggerApiInfo) var2.next();
            ApiInfo apiInfo = (new ApiInfoBuilder()).title(swaggerApiInfo.getGroupName()).description("").termsOfServiceUrl("").version(swaggerApiInfo.getVersion()).build();
            Docket docket = (new Docket(DocumentationType.SWAGGER_2)).groupName(swaggerApiInfo.getGroupName()).enable(this.swaggerEnable()).apiInfo(apiInfo).select().apis(RequestHandlerSelectors.basePackage(swaggerApiInfo.getBasePackage())).paths(PathSelectors.any()).build();
            this.docketList.add(docket);
        }

    }

    @Bean
    public Docket createRestApiZero() {

        if (this.docketList == null) {
            this.initDockets();
        }

        return (Docket) this.docketList.get(0);
    }

    @Bean
    public Docket createRestApiOne() {
        return (Docket) this.docketList.get(1);
    }

    @Bean
    public Docket createRestApiTwo() {
        return (Docket) this.docketList.get(2);
    }

    @Bean
    public Docket createRestApiThree() {
        return (Docket) this.docketList.get(3);
    }

    @Bean
    public Docket createRestApiFour() {
        return (Docket) this.docketList.get(4);
    }

    @Bean
    public Docket createRestApiFive() {
        return (Docket) this.docketList.get(5);
    }

    @Bean
    public Docket createRestApiSix() {
        return (Docket) this.docketList.get(6);
    }

    @Bean
    public Docket createRestApiSeven() {
        return (Docket) this.docketList.get(7);
    }

    @Bean
    public Docket createRestApiEight() {
        return (Docket) this.docketList.get(8);
    }

    @Bean
    public Docket createRestApiNine() {
        return (Docket) this.docketList.get(9);
    }

    @Bean
    public Docket createRestApi10() {
        return (Docket) this.docketList.get(10);
    }

    @Bean
    public Docket createRestApi11() {
        return (Docket) this.docketList.get(11);
    }

    @Bean
    public Docket createRestApi12() {
        return (Docket) this.docketList.get(12);
    }

    @Bean
    public Docket createRestApi13() {
        return (Docket) this.docketList.get(13);
    }

    @Bean
    public Docket createRestApi14() {
        return (Docket) this.docketList.get(14);
    }
}
