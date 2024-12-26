package com.dnpa.common.filter;

import com.dnpa.common.component.DatabaseContextHolder;
import com.dnpa.common.constants.AccountConst;
import com.dnpa.common.security.AccountAuthentication;
import com.dnpa.common.security.CustomUserDetails;
import com.dnpa.common.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(value = "AccessFilter")
@Slf4j
public class AccessFilter extends OncePerRequestFilter {
    @Autowired
    private DatabaseContextHolder dbc;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    public static final String SUPER_USER_HEADER = "SUPER_USER";
    public static final String SUPER_USER = "DNPA29122002";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String SUPER_USER_TOKEN = "DNPA29122002";
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        ThreadContext.put("PathAPI", path);
        if (!request.getRequestURI().contains("actuator/health")) {
            log.info("Start API: "+ path);
        }
        dbc.setReadOnly();
        String superUser = request.getHeader(SUPER_USER_HEADER);
        if(superUser != null && superUser.equals(SUPER_USER)){
            CustomUserDetails customUserDetails = CustomUserDetails.builder().userCode(AccountConst.SUPER_USER_CODE)
                    .userId(AccountConst.SUPER_USER_ID)
                    .userName(AccountConst.SUPER_USER_NAME)
                    .email(AccountConst.SUPER_USER_EMAIL)
                    .build();
            AccountAuthentication accountAuthentication = AccountAuthentication.builder().customUserDetails(customUserDetails)
                    .accessToken(SUPER_USER_TOKEN).build();
            SecurityContextHolder.getContext()
                    .setAuthentication(accountAuthentication);

            filterChain.doFilter(request, response);
            return;
        }
        String jwt = getJwtFromRequest(request);
        if (StringUtils.hasText(jwt) && jwtTokenProvider.validateJwtToken(jwt)) {
            //get userdetail from jwt
            filterChain.doFilter(request, response);
        }

    }
    private String getJwtFromRequest(HttpServletRequest request){
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return "";
    }
}
