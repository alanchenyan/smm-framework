package com.smm.framework;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Alan Chen
 * @description
 * @date 2020-01-09
 */
public class BcryptPassword {

    public static void main (String[] args){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode("admin");
        System.out.println(encodedPassword);
    }
}
