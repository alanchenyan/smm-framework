#### 前言
smm-framework（以后简称smm框架） 是由SpringBoot + Mybatis Plus + Mysql 组成的一套开发框架。该套框架基于约定大于配置的原则实现了大部分项目开发中需要用到的功能模块，比如接口权限控制JWT Token、MybatisPlus集成与配置、Swagger继承与配置、跨域配置、全局异常处理、API接口实现统一格式返回等等。smm框架基于Jdk 1.8、SpringBoot 2.1.8开发，同时遵循《阿里巴巴Java开发手册V1.5》、API接口遵循Restful风格。

#### 一、在项目中引入smm框架
##### 1.1 输入网址https://jitpack.io
##### 1.2 搜索框中输入alanchenyan/smm-framework
##### 1.3 可以在当前页面中查看到smm框架所有历史版本以及导入方式

**以maven导入方式为例**

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

Step 3. Set parent
```
<parent>
    <groupId>com.github.alanchenyan</groupId>
    <artifactId>smm-framework</artifactId>
    <version>1.1.7</version>
    <relativePath/> 
</parent>
```
需要重点说明的是，如果你的项目中将parent配置成了smm框架（即Step 3），那么smm框架中已经配置导入的依赖，你的项目中不需要再重新配置依赖，如spring boot、mysql-connector-java、mybatis plus、swagger2、hibernate-validator等等。

#### 二、功能使用说明

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

##### 2.3 mybatis plus配置
继承GlobalMybatisConfig
```
@Configuration
public class MybatisPlusConfig extends GlobalMybatisConfig {

}
```

##### 2.4 Swagger文档配置
继承GloablSwaggerConfig
```
@Configuration
@EnableSwagger2
public class SwaggerConfig extends GloablSwaggerConfig {

}
```

另外，Swagger通常只在本地开发环境或内网测试环境开启，生产环境中Swagger一般都是需要关闭的。因此我们提供了一个抽象方法boolean swaggerEnable()需要你来指定Swagger是否开启，通常这个开关配置在配置文件中，开发环境设置为true,生产设置成true，如下：

```
@Configuration
@EnableSwagger2
public class SwaggerConfig extends GloablSwaggerConfig {
    @Value("${swagger.enable}")
    private boolean enable;

    @Override
    public String swaggerTile() {
        return "网值Boss API列表";
    }

    @Override
    public boolean swaggerEnable() {
        return enable;
    }
}
```

##### 2.5 接口权限控制JWT Token配置

###### 2.5.1 实现接口UserDetailsService,重写loadUserByUsername()方法，查询数据库返回UserDetails

密码加密方式默认用的是BCryptPasswordEncoder
```
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    IUserAdminService userAdminServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        String password = userAdminServiceImpl.getUserPassword(userName);
        return new User(userName, password, getAuthority());
    }

    private List getAuthority() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
```

###### 2.5.2 继承GlobalWebSecurityConfigurer
```
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class WebSecurityConfigurer extends GlobalWebSecurityConfigurer {

}
```
为了方便开发，一般我们在本地开发环境中会关闭接口权限校验，因为我们提供了一个customCloseAuthorityEvironment()方法，你可以指定某种环境下关闭接口权限校验，如下：
```
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class WebSecurityConfigurer extends GlobalWebSecurityConfigurer {

    @Value("${spring.profiles.active}")
    private String currentRunEnvironment;

    /**
     * 指定某种运行环境下关闭权限校验；为了方便开发，一般我们的dev环境会关闭接口权限校验
     * @return
     */
    @Override
    public CloseAuthorityEvironment customCloseAuthorityEvironment(){
        return new CloseAuthorityEvironment(currentRunEnvironment,"dev");
    }
}
```


