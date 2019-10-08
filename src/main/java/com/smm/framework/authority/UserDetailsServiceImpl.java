//package com.smm.framework.authority;
//
//import com.smm.framework.authority.service.IAuthLoginService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
///**
// * @Author -Huang
// * @create 2019/9/4 10:38
// */
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//
//    @Autowired
//    IAuthLoginService authLoginServiceImpl;
//
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        LoginAccount account = authLoginServiceImpl.getLoginAccount(userName);
//        if(null == account){
//            throw new UsernameNotFoundException("授权账号不存在");
//        }
//        return new User(account.getAccountName(),account.getPassword(),null);
//    }
//
//}
