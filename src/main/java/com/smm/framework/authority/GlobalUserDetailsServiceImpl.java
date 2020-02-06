package com.smm.framework.authority;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alan Chen
 * @description 校验账号
 * @date 2020-02-05
 */
public class GlobalUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    public HttpServletRequest request;

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
