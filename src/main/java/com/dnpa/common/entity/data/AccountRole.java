package com.dnpa.common.entity.data;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Entity
public class AccountRole {
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 0;
    public static final int ROLE_USER = 1;
    public static final int ROLE_ADMIN = 2;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;
    private Integer roleId;
    private Date startDate;
    private Date endDate;
    private int status;
}
