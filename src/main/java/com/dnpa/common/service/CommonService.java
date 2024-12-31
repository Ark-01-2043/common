package com.dnpa.common.service;

import com.dnpa.common.security.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class CommonService {
    private CustomUserDetails getUserPrincipal() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        }
        throw new AuthenticationCredentialsNotFoundException("Can not find logged in user");
    }
}
