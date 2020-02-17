package com.smm.framework.authority;

import com.smm.framework.authority.exception.GlobalAccessDeniedHandler;
import com.smm.framework.authority.exception.GlobalAuthenticationEntryPoint;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author Alan Chen
 * @description
 * @date 2019-10-12
 */
@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER)
public class GlobalWebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    AuthenticationEntryPoint globalAuthenticationEntryPoint = new GlobalAuthenticationEntryPoint();

    AccessDeniedHandler globalAccessDeniedHandler = new GlobalAccessDeniedHandler();


    /**
     * JWT 私钥
     * @return
     */
    protected String signingKey(){
        return "PrivateSecret";
    }


    /**
     * 登录时用RSA加密的私钥
     * @return
     */
    protected String loginEncryptRsaPrivateKey(){
        return null;
    }

    /**
     * JWT Token有效期时间
     * @return
     */
    protected long expirationTime(){
        return 180 * 60 * 1000;
    }

    /**
     * 如果要让某种运行环境下关闭权限校验，请重写该方法
     * @return
     */
    protected CloseAuthorityEvironment customCloseAuthorityEvironment(){
        return null;
    }

    /**
     * 密码加密及校验方式
     *
     * @return
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Web资源权限控制
     *
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/config/**", "/css/**", "/fonts/**", "/img/**","/image/**","/images/**","/static/**", "/js/**");

        //swagger-ui start
        web.ignoring().antMatchers("/v2/api-docs/**");
        web.ignoring().antMatchers("/swagger.json");
        web.ignoring().antMatchers("/swagger-ui.html");
        web.ignoring().antMatchers("/swagger-resources/**");
        web.ignoring().antMatchers("/webjars/**");
        //swagger-ui end
    }

    /**
     * HTTP请求权限控制
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        boolean isCloseAuth = false;

        CloseAuthorityEvironment closeAuthority = customCloseAuthorityEvironment();
        if(closeAuthority ==null || closeAuthority.getCloseAuthEnvironments() == null || closeAuthority.getCurrentRunEnvironment()==null){
            isCloseAuth = false;
        }else{
            String[] closeAuthEnvironmentArry = closeAuthority.getCloseAuthEnvironments();
            if(closeAuthEnvironmentArry != null){
                for(String closeAuthEnvironment : closeAuthEnvironmentArry){
                    if(closeAuthority.getCurrentRunEnvironment().equals(closeAuthEnvironment)){
                        isCloseAuth = true;
                        break;
                    }
                }
            }
        }

        if(isCloseAuth){
            closeAuthConfigure(http);
        }else{
            customConfigure(http);
            commonConfigure(http);
        }
    }


    /**
     * 用户自定义配置，子类可覆盖自定义实现
     * @param http
     * @throws Exception
     */
    protected HttpSecurity customConfigure(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable().authorizeRequests()
                .anyRequest().authenticated()
                .and();
        return http;
    }


    private void commonConfigure(HttpSecurity http) throws Exception{

        GlobalUsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = new GlobalUsernamePasswordAuthenticationFilter(authenticationManager(),signingKey(),expirationTime(),loginEncryptRsaPrivateKey());
        GlobalBasicAuthenticationFilter basicAuthenticationFilter = new GlobalBasicAuthenticationFilter(authenticationManager(),signingKey());

        http.addFilter(usernamePasswordAuthenticationFilter)
                .addFilter(basicAuthenticationFilter);

        http.exceptionHandling().accessDeniedHandler(globalAccessDeniedHandler).authenticationEntryPoint(globalAuthenticationEntryPoint);
        // 禁用 SESSION、JSESSIONID
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    /**
     * 关闭接口权限校验配置
     * @param http
     * @throws Exception
     */
    private void closeAuthConfigure(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/**").permitAll();
    }

}