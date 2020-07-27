package com.smm.framework.authority;

import com.alibaba.fastjson.JSONObject;
import com.smm.framework.authority.rsa.RsaUtil;
import com.smm.framework.constant.GlobalConstants;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Alan Chen
 * @description
 * @date 2019-10-12
 *
 */
public class GlobalUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    /**
     * JWT Token 过期日期
     */
    private Date expirationDate;

    /**
     * 参数对象
     */
    private UsernamePasswordAuthParameter parameter;


    public GlobalUsernamePasswordAuthenticationFilter(UsernamePasswordAuthParameter parameter) {
        this.parameter = parameter;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = this.obtainUsername(request);
        String password = this.obtainPassword(request);

        if(StringUtils.isNotBlank(parameter.getLoginEncryptRsaPrivateKey())){

            String clientType = request.getHeader(GlobalConstants.CLIENT_LOGIN_TYPE);
            List<String> loginEncryptRsaClientType = parameter.getLoginEncryptRsaClientType();

            if(loginEncryptRsaClientType !=null && loginEncryptRsaClientType.size()>0){
                if(loginEncryptRsaClientType.contains(clientType)){
                    username = RsaUtil.privateDecrypt(username,parameter.getLoginEncryptRsaPrivateKey());
                    password = RsaUtil.privateDecrypt(password,parameter.getLoginEncryptRsaPrivateKey());
                }
            }
        }

        return parameter.getAuthenticationManager().authenticate(
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
        String token;
        if(parameter.isTokenNeverExpires()){
            token = Jwts.builder()
                    .setSubject(((User) authResult.getPrincipal()).getUsername())
                    .setExpiration(getExpiration())
                    .signWith(SignatureAlgorithm.HS512, parameter.getSigningKey())
                    .compact();
        }else{
             token = Jwts.builder()
                    .setSubject(((User) authResult.getPrincipal()).getUsername())
                    .setExpiration(new Date(System.currentTimeMillis() +  parameter.getExpirationTime()))
                    .signWith(SignatureAlgorithm.HS512, parameter.getSigningKey())
                    .compact();
        }


        returnToken(response, JwtUtil.getTokenHeader(token));
    }

    private Date getExpiration(){
        if(expirationDate!=null){
            return expirationDate;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            expirationDate = simpleDateFormat.parse("3000-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return expirationDate;
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
