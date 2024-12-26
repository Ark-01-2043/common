package com.dnpa.common.security;

import com.dnpa.common.enums.AccountStatus;
import com.dnpa.common.util.DateTimeUtil;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.nio.file.attribute.UserPrincipal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@Builder
public class CustomUserDetails implements Serializable, UserDetails {
    private Long userId = 0L;
    private String userCode = "";
    private String userName = "";
    private String fullName = "";
    private String email = "";
    private AccountStatus status = AccountStatus.ACTIVE;
    private String startDate = null;
    private String endDate = null;
    private String startBanDate = null;
    private String endBanDate = null;
    private List<Integer> roles;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return "";
    }


    @Override
    public String getUsername() {
        return String.valueOf(userId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return status != AccountStatus.INACTIVE && DateTimeUtil.isFutureDate(endDate);
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != AccountStatus.BANNED && DateTimeUtil.isFutureDate(endBanDate);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return isAccountNonExpired() && isAccountNonLocked();
    }


}
