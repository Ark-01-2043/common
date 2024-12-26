package com.dnpa.common.entity.dto;

import com.dnpa.common.security.CustomUserDetails;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Request {
    private String method;
    private String url;
    private String timestamp;
    private Map<String, String> parameters;
    private String body;
    private CustomUserDetails userDetails;
}
