package com.dnpa.common.config.message;

import com.dnpa.common.util.MessageUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageConfig {
    public static final String AUTH_MESSAGE_URL = "classpath:/message/authMessage";
    public static final String ERROR_MESSAGE_URL = "classpath:/message/errorMessage";
    public static final String WARNING_MESSAGE_URL = "classpath:/message/warningMessage";
    public static final String EXCEPTION_MESSAGE_URL = "classpath:/message/exceptionMessage";
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasenames(AUTH_MESSAGE_URL, ERROR_MESSAGE_URL, WARNING_MESSAGE_URL, EXCEPTION_MESSAGE_URL);
        messageSource.setDefaultEncoding("UTF-8");

        MessageUtil.init(messageSource);

        return messageSource;
    }




}
