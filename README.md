#### 前言
smm-framework（以后简称smm框架） 是由SpringBoot + Mybatis Plus + Mysql 组成的一套开发框架。该套框架基于约定大于配置的原则实现了大部分项目开发中需要用到的功能模块，比如接口权限控制JWT Token、MybatisPlus集成与配置、Swagger继承与配置、跨域配置、全局异常处理、API接口实现统一格式返回等等。smm框架基于Jdk 1.8、SpringBoot 2.1.8开发，同时遵循《阿里巴巴Java开发手册V1.5》、API接口遵循Restful风格。

#### 一、在项目中引入smm框架
##### 1.1 输入网址https://jitpack.io
##### 1.2 搜索框中输入alanchenyan/smm-framework
##### 1.3 可以在当前页面中查看到smm框架所有历史版本以及导入方式

**gradle导入方式**

Step 1. Add the JitPack repository to your build file
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Step 2. Add the dependency
```
dependencies {
   implementation 'com.github.alanchenyan:smm-framework:1.1.5'
}
```

**maven导入方式**

Step 1. Add the JitPack repository to your build file
```
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```
Step 2. Add the dependency
```
<dependency>
    <groupId>com.github.alanchenyan</groupId>
    <artifactId>smm-framework</artifactId>
    <version>1.1.5</version>
</dependency>
```

#### 二、功能使用说明

以下相关功能的集成以maven为例

##### 2.1 跨域请求配置
继承GlobalCorsConfig
```
@Configuration
@EnableWebMvc
public class CorsConfig extends GlobalCorsConfig {

}
```


##### 2.2 全局异常处理
继承GlobalExceptionHandler
```
@ControllerAdvice
@Component
public class ExceptionHandler extends GlobalExceptionHandler {

}
```

##### 2.3 mybatis plus集成与配置

###### 2.3.1 导入mybatis plus依赖
```
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.0.3</version>
</dependency>
```
###### 2.3.2 继承GlobalMybatisConfig
```
@Configuration
public class MybatisPlusConfig extends GlobalMybatisConfig {

}
```

##### 2.4 swagger集成与配置

###### 2.4.1 导入swagger依赖

