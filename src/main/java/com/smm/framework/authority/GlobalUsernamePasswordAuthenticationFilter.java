package com.smm.framework.authority;

import com.alibaba.fastjson.JSONObject;
import com.smm.framework.authority.rsa.RsaUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alan Chen
 * @description
 * @date 2019-10-12
 */
public class GlobalUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    //JWT 私钥
    private String signingKey;

    //JWT Token有效期时间
    private long expirationTime;

    //登录时用RSA加密的私钥
    private String loginEncryptRsaPrivateKey;

    public GlobalUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,String signingKey,long expirationTime,String loginEncryptRsaPrivateKey) {
        this.authenticationManager = authenticationManager;
        this.signingKey = signingKey;
        this.expirationTime = expirationTime;
        this.loginEncryptRsaPrivateKey = loginEncryptRsaPrivateKey;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);

        if(StringUtils.isNotBlank(loginEncryptRsaPrivateKey)){
            username = RsaUtil.privateDecrypt(username,loginEncryptRsaPrivateKey);
            password = RsaUtil.privateDecrypt(password,loginEncryptRsaPrivateKey);
        }

        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password,
                        new ArrayList<>()
                )
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) {
        String token = Jwts.builder()
                .setSubject(((User) authResult.getPrincipal()).getUsername())
                .setExpiration(new Date(System.currentTimeMillis() +  expirationTime))
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();

        returnToken(response, JwtUtil.getTokenHeader(token));
    }

    private void returnToken(HttpServletResponse response, String token) {

        Map<String,Object> tokenMap = new HashMap(3);

        tokenMap.put("code",200);
        tokenMap.put("msg","");
        tokenMap.put("data",token);

        JSONObject responseJsonObject = new JSONObject(tokenMap);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(responseJsonObject.toString());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
