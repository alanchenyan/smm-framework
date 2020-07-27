package com.smm.framework.authority;

import lombok.Data;
import org.springframework.security.authentication.AuthenticationManager;
import java.util.List;

/**
 * @author Alan Chen
 * @description 权限参数
 * @date 2020-07-27
 */
@Data
public class UsernamePasswordAuthParameter {

    /**
     * 管理器
     */
    private AuthenticationManager authenticationManager;

    /**
     * JWT 私钥
     */
    private String signingKey;

    /**
     * JWT Token有效期时间
     */
    private long expirationTime;

    /**
     * JWT Token有 是否永不过期
     */
    private boolean tokenNeverExpires;

    /**
     * 登录时用RSA加密的私钥
     */
    private String loginEncryptRsaPrivateKey;

    /**
     * 登录时用RSA加密的登录类型
     */
    List<String> loginEncryptRsaClientType;

}
