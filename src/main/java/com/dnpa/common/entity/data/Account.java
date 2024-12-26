package com.dnpa.common.entity.data;

import com.dnpa.common.constants.AccountConst;
import com.dnpa.common.enums.AccountStatus;
import lombok.Builder;
import lombok.Data;
import org.hibernate.Hibernate;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Builder
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long updateCount = 0L;
    @Column(unique = true)
    private String userCode = "";
    @Column(unique = true)
    private String userName = "";
    private String password = "";
    private String fullName = "";
    @Column(unique = true)
    private String email = "";
    private int status = AccountConst.ACTIVE_ACCOUNT_INT;
    private Date startDate = null;
    private Date endDate = null;
    private Date startBanDate = null;
    private Date endBanDate = null;
    @OneToMany(targetEntity = AccountRole.class, cascade = CascadeType.ALL, mappedBy = "account")
    private List<AccountRole> roles;
    public boolean isAdmin(){
        Hibernate.initialize(this.getRoles());
        return this.getRoles().stream().filter(e -> AccountRole.ROLE_ADMIN == e.getRoleId() && AccountRole.ACTIVE == e.getStatus()).count() > 0;
    }
    public boolean isUser(){
        Hibernate.initialize(this.getRoles());
        return this.getRoles().stream().filter(e -> AccountRole.ROLE_USER == e.getRoleId() && AccountRole.ACTIVE == e.getStatus()).count() > 0;
    }
}
