package com.smm.framework.authority;

import com.smm.framework.exception.ServiceException;
import com.smm.framework.i18n.I18nResource;
import com.smm.framework.i18n.I18nResourceFactory;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Alan Chen
 * @description
 * @date 2019-10-12
 */
public class GlobalBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private I18nResource i18nResource = I18nResourceFactory.getI18nResource();

    private String signingKey;

    public GlobalBasicAuthenticationFilter(AuthenticationManager authenticationManager,String signingKey) {
        super(authenticationManager);
        this.signingKey = signingKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith(JwtUtil.getAuthorizationHeaderPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authenticationToken = getUsernamePasswordAuthenticationToken(header);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token) {
        String user = Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token.replace(JwtUtil.getAuthorizationHeaderPrefix(), ""))
                .getBody()
                .getSubject();

        if (null != user) {
            return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        }else{
            throw new ServiceException(i18nResource.getValue("authentication_failed"));
        }
    }
}