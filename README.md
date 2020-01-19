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

#### 2.1 跨域请求配置
继承GlobalCorsConfig
```
@Configuration
@EnableWebMvc
public class CorsConfig extends GlobalCorsConfig {

}
```


#### 2.2 全局异常处理
继承GlobalExceptionHandler
```
@ControllerAdvice
@Component
public class ExceptionHandler extends GlobalExceptionHandler {

}
```

#### 2.3 mybatis plus配置
继承GlobalMybatisConfig
```
@Configuration
public class MybatisPlusConfig extends GlobalMybatisConfig {

}
```
数据表的创建时间、修改时间会自动填充当前的系统时间，如果你的数据表有记录操作人和修改人，需要在请求的header里传入userId参数，smm框架会自动在数据表中记录操作人、修改人。注意，要写FieldFill.INSERT和INSERT_UPDATE注解，如下：
```
@Data
public class BaseEntity {

    @ApiModelProperty("ID（后台自动插入）")
    @TableId(type = IdType.UUID)
    private String id;

    @JsonIgnore
    @ApiModelProperty("创建人ID（后台自动插入）")
    @TableField(fill = FieldFill.INSERT)
    private String createUserId;

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("数据创建时间（后台自动生成）")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty("修改人ID（后台自动插入）")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updateUserId;

    @JSONField(serialize = false)
    @JsonIgnore
    @ApiModelProperty("数据修改时间（后台自动生成）")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @JsonIgnore
    @JSONField(serialize = false)
    @ApiModelProperty("逻辑删除标志 0:未删除；1：已删除（后台自动插入）")
    @TableLogic
    private Integer deleted;
}

```

#### 2.4 Swagger文档配置
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


#### 2.5 接口权限控制JWT Token配置

##### 2.5.1 实现接口UserDetailsService,重写loadUserByUsername()方法，查询数据库返回UserDetails

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

##### 2.5.2 继承GlobalWebSecurityConfigurer
```
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class WebSecurityConfigurer extends GlobalWebSecurityConfigurer {

}
```

##### 2.5.3 指定某种环境下关闭接口权限校验
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

##### 2.5.4 JWT Token signingKey
重写String signingKey()方法，返回你的私钥

##### 2.5.5 设置Token过期时间
重写long expirationTime()方法,如：
```
    protected long expirationTime(){
        return 180 * 60 * 1000; //token有效期为3小时
    }
```

##### 2.5.6 自定义放行接口
如果你需要指定某些接口要放行，你可以重写customConfigure(HttpSecurity http)，通过HttpSecurity设置放行接口，然后返回设置后的HttpSecurity

##### 2.5.7 登录用RSA非对称性加密对用户名、密码进行加密安全传输
 重写 GlobalWebSecurityConfigurer类的protected String loginEncryptRsaPrivateKey()方法，配置你的RSA私钥。同时前端也需要配合用对应的RSA公钥对用户名、密码进行加密。
详情可参考：[用非对称性加密保障Web登录安全](https://www.jianshu.com/p/0fe3a7ae2256)

#### 2.6 Restful API 返回统一的数据格式到前端

##### 2.6.1 smm框架中，统一返回到前端的格式是ResponseResult
```
public class ResponseResult {
    private int code;
    private String msg;
    private Object data;
}
```

##### 2.6.2 server端的异常也会被全局拦截，统一返回ResponseResult格式
参见2.2

##### 2.6.3 全局拦截Controller层API，对所有返回值统一包装成ResponseResult格式再返回到前端
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

#### 2.7 国际化

##### 2.7.1 服务器返回到客户端消息/异常的国际化
第一步：继承GlobalExceptionHandler类，重写enableResponseMessageI18n方法
```
@ControllerAdvice
@Component
public class ExceptionHandler extends GlobalExceptionHandler {
    @Override
    protected boolean enableResponseMessageI18n() {
        return true;
    }
}
```
第二步：在resources/i18n/ 目录下新建Resource Bundel,名字为：messages。配置键值对，如
```
not_fund_data=数据不存在
password_error=您的登录密码错误
```

第三步：使用国际化，如
```
    public User getUser(String id) {
        User user = userDao.selectById(id);
        if(user == null){
            throw new ServiceException("not_fund_data");
        }
        return user;
    }
```

第四步：如果要自定义国际化文件的路径，可以继承GlobalExceptionHandler类，重写responseMessageI18nSourcePath方法
```
@ControllerAdvice
@Component
public class ExceptionHandler extends GlobalExceptionHandler {
    @Override
    protected String responseMessageI18nSourcePath(){
        return "i18n/messages";
    }
}
```

##### 2.7.2 hibernate-validator 校验提示信息的国际化

第一步：继承GlobalExceptionHandler类，重写enableValidationI18n方法
```
@ControllerAdvice
@Component
public class ExceptionHandler extends GlobalExceptionHandler {

    @Override
    protected boolean enableValidationI18n() {
        return true;
    }
}
```

第二步：在resources/i18n/ 目录下新建Resource Bundel,名字为：validation。配置键值对，如
```
app_user_inputphone=请您输入手机号码
app_user_input_areacode=请您选择区号
```

第三步：使用国际化
```
@Data
public class UserRegistVO {

    @NotBlank(message = "app_user_input_areacode")
    @ApiModelProperty("区号")
    private String areaCode;

    @NotBlank(message = "app_user_input_phone")
    @ApiModelProperty("手机号")
    private String phone;
    
     ......
}
```

第四步：如果要自定义国际化文件的路径，可以继承GlobalExceptionHandler类，重写validationI18nSourcePath方法
```
@ControllerAdvice
@Component
public class ExceptionHandler extends GlobalExceptionHandler {
    @Override
    protected String validationI18nSourcePath(){
        return "i18n/validation";
    }
}
```

#### 2.8 文件上传功能（图片/pdf等任意文件类型）
第一步：上传文件
调用FileUpLoadTool工具类的相关方法，如
```
public String uploadFile(MultipartFile file) {
      return FileUpLoadTool.uploadFile(file);
}
```
FileUpLoadTool工具类上传的文件默认存储在当前项目的uploadfiles目录，如果没有该目录，则会自动创建该目录。当然也可以自定义存储目录，可以指定的存储目录传给uploadFile方法，如
```
public String uploadFile(MultipartFile file) {
      return FileUpLoadTool.uploadFile(file,"/data/images/");
}
```
同时还可以设置限制上传文件大小的参数，参考uploadFile的其他重构方法。

第二步：配置访问文件映射
```
@Configuration
@EnableWebMvc
public class UploadFileConfig extends GlobalUploadFileConfig {
}

```

第三步：访问上传的文件
文件上传后，uploadFile方法会返回文件的存储名字，且会加上默认的访问前缀是files，访问文件（或图片）的方式为：
服务器地址+files+图片名称，如下：
```
http://127.0.0.01:8080/files/34334EFfe1323.png
```


 

