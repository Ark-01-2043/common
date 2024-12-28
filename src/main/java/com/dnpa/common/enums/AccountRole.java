package com.dnpa.common.enums;

import com.dnpa.common.exception.NotFoundResource;

public enum AccountRole {
    USER(1, "USER"),
    ADMIN(2, "ADMIN"),
    MANAGER(3, "MANAGER");
    private final int roleId;
    private final String roleName;
    public static final int RESOURCE_ID = 2;
    AccountRole(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
    public static AccountRole getAccountRole(int roleId){
        for (AccountRole accountRole: AccountRole.values()){
            if (accountRole.roleId == roleId){
                return accountRole;
            }
        }
        throw new NotFoundResource(RESOURCE_ID, roleId);
    }
}
