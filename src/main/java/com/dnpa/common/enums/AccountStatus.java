package com.dnpa.common.enums;

import com.dnpa.common.exception.NotFoundResource;

public enum AccountStatus {
    ACTIVE(1, "ACTIVE"),
    INACTIVE(0, "INACTIVE"),
    BANNED(-1, "BANNED");
    private final int statusId;
    private final String statusName;
    public static final int RESOURCE_ID = 1;
    AccountStatus(int statusId, String statusName) {
        this.statusId = statusId;
        this.statusName = statusName;
    }
    public static AccountStatus getAccountStatus(int statusId){
        for (AccountStatus accountStatus: AccountStatus.values()){
            if (accountStatus.statusId == statusId){
                return accountStatus;
            }
        }
        throw new NotFoundResource(RESOURCE_ID, statusId);
    }
}
