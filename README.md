#### 前言
smm-framework（以后简称smm框架） 是由SpringBoot + Mybatis Plus + Mysql 组成的一套开发框架。该套框架基于约定大于配置的原则实现了大部分项目开发中需要用到的功能模块，比如接口权限控制JWT Token、MybatisPlus集成与配置、Swagger继承与配置、跨域配置、全局异常处理、API接口实现统一格式返回等等。smm框架基于Jdk 1.8、SpringBoot 2.1.8开发，同时遵循《阿里巴巴Java开发手册V1.5》、API接口遵循Restful风格。

#### 一、在项目中引入smm框架

**以maven导入方式为例**

##### 1.1 配置jitpack.io的repository
smm-framework-parent、smm-framework都发布在jitpack.io，所以要配置jitpack.io的repository
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

##### 1.2 parent设置为smm-framework-parent
```
<parent>
    <groupId>com.github.alanchenyan</groupId>
    <artifactId>smm-framework-parent</artifactId>
    <version>1.0.2</version>
</parent>
```
smm-framework-parent是smm框架父级依赖，smm-framework-parent已经将所用框架的依赖都配置好了，你的项目中只要将parent设置为smm-framework-parent进行依赖传递，spring boot、mysql-connector-java、mybatis plus、swagger2、hibernate-validator等等都不需要配置了，在你的项目中只需要配置你项目中独有的第三方框架。

##### 1.3依赖smm框架
```
<dependency>
    <groupId>com.github.alanchenyan</groupId>
    <artifactId>smm-framework</artifactId>
    <version>1.2.5</version>
</dependency>
```

##### 拓展：对jitpack.io介绍

- 输入网址https://jitpack.io

- 搜索框中输入alanchenyan/smm-framework 或 alanchenyan/smm-framework-parent，可以在当前页面中查看到smm框架所有历史版本以及导入方式


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

##### 2.4.1 设置是否开启Swagger
Swagger通常只在本地开发环境或内网测试环境开启，生产环境中Swagger一般都是需要关闭的。因此我们提供了一个抽象方法boolean swaggerEnable()需要你来指定Swagger是否开启，通常这个开关配置在配置文件中，开发环境设置为true,生产设置成false，如下：

```
@Configuration
@EnableSwagger2
public class SwaggerConfig extends GloablSwaggerConfig {
    @Value("${swagger.enable}")
    private boolean enable;

    @Override
    public boolean swaggerEnable() {
        return enable;
    }
}
```

##### 2.4.2 设置多个Docket
有时候API会分为多个模块，因此需要对API进行分组显示，此时可以重写configureSwaggerApiInfo（）方法，返回多个你需要的ApiInfo，例如：
```
@Override
protected List<SwaggerApiInfo> configureSwaggerApiInfo() {

    List<SwaggerApiInfo> swaggerApiInfos = new ArrayList<>();

    SwaggerApiInfo userModelApiInfo = new SwaggerApiInfo("用户模块API接口文档","com.netx.user.controller","V1.0");
    swaggerApiInfos.add(userModelApiInfo);

    SwaggerApiInfo lawyerModelApiInfo = new SwaggerApiInfo("律师模块API接口文档","com.netx.lawyer.controller","V1.0.1");
    swaggerApiInfos.add(lawyerModelApiInfo);

    return swaggerApiInfos;
}
```
暂时最多支持配置10个Docket，正常情况下10个已经足够了，如果需要，smm框架还能提供更多的配置数量。


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

###### 2.5.3 指定某种环境下关闭接口权限校验
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

###### 2.5.4 JWT Token signingKey
重写String signingKey()方法，返回你的私钥

###### 2.5.5 设置Token过期时间
重写Date expirationDate()方法

###### 2.5.6 自定义放行接口
如果你需要指定某些接口要放行，你可以重写customConfigure(HttpSecurity http)，通过HttpSecurity设置放行接口，然后返回设置后的HttpSecurity


##### 2.6 Restful API 返回统一的数据格式到前端

###### 2.6.1 smm框架中，统一返回到前端的格式是ResponseResult
```
public class ResponseResult {
    private int code;
    private String msg;
    private Object data;
}
```

###### 2.6.2 server端的异常也会被全局拦截，统一返回ResponseResult格式
参见2.2

###### 2.6.3 全局拦截Controller层API，对所有返回值统一包装成ResponseResult格式再返回到前端
继承GlobalReturnConfig
```
@EnableWebMvc
@Configuration
@RestControllerAdvice({"com.netx.web.controller"})
public class ControllerReturnConfig  extends GlobalReturnConfig {

}
```

注意：@RestControllerAdvice要设置扫描拦截包名，如：com.netx.web.controller。这样就只拦截controller包下的类。否则swagger也会拦截影响swagger正常使用


全局拦截后Controller层API不需要显示地返回ResponseResult，因为会全局拦截处理并返回ResponseResult格式，如一下代码

```
 @ApiOperation("新增代理")
  @PostMapping
  public ResponseResult createAgents(@RequestBody @Valid AddUserAgentVO addUserAgentVO){
      userAgentServiceImpl.saveAgents(addUserAgentVO);
      return ResponseResult.success();
  }
```
可以改成
```
@ApiOperation("新增代理")
@PostMapping
public void createAgents(@RequestBody @Valid AddUserAgentVO addUserAgentVO){
    userAgentServiceImpl.saveAgents(addUserAgentVO);
}
```
代码
```
@ApiOperation("获取代理列表")
@GetMapping("/list")
public ResponseResult pageUserAgents(UserAgentSearch search) {
    IPage<UserAgentVO> page = userAgentServiceImpl.pageUserAgents(search);
    return ResponseResult.success(page);
}
```
可以改成
```
@ApiOperation("获取代理列表")
@GetMapping("/list")
public IPage<UserAgentVO> pageUserAgents(UserAgentSearch search) {
    return userAgentServiceImpl.pageUserAgents(search);
}
```






 

