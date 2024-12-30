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
    private Long updateCount;
    @Column(unique = true)
    private String userCode;
    @Column(unique = true)
    private String userName;
    private String password;
    private String fullName;
    @Column(unique = true)
    private String email;
    private int status;
    private Date startDate;
    private Date endDate;
    private Date startBanDate;
    private Date endBanDate;
    @OneToMany(targetEntity = AccountRole.class, cascade = CascadeType.ALL, mappedBy = "account")
    private List<AccountRole> roles;

}
