package com.dnpa.common.exception;

import com.dnpa.common.constants.ExceptionMessageCode;
import com.dnpa.common.util.MessageUtil;

public class NotFoundResource extends RuntimeException {
    public static final String ACCOUNT_STATUS = "ACCOUNT STATUS";
    public static final String ACCOUNT_ROLE = "ACCOUNT ROLE";
    public static final String NOT_FOUND = "NOT FOUND";
    public NotFoundResource(String message) {
        super(message);
    }
    public NotFoundResource(int resourceId, Object value) {
        // add message via resourceId
        super(MessageUtil.get(ExceptionMessageCode.NOT_FOUND_RESOURCE, value.toString(), resourceId));
    }
    public static String getResourceName(int resourceId){
        switch (resourceId) {
            case 1:
                return ACCOUNT_STATUS;
            case 2:
                return ACCOUNT_ROLE;
            default:
                return NOT_FOUND;
        }
    }
}
