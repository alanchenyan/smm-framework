package com.smm.framework.config.swagger;

import lombok.Data;

/**
 * @author Alan Chen
 * @description 配置Swagger Docket 的信息
 * @date 2019-10-14
 */
@Data
public class SwaggerApiInfo {

    private String groupName;

    private String basePackage;

    private String version;

    public SwaggerApiInfo() {
        super();
    }

    public SwaggerApiInfo(String groupName, String basePackage, String version) {
        this.groupName = groupName;
        this.basePackage = basePackage;
        this.version = version;
    }
}
