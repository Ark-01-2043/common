package com.dnpa.common.util;

import org.springframework.context.MessageSource;

import java.util.Locale;

public class MessageUtil {
    private static MessageSource messageSource;
    private static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
    public static void init(MessageSource messageSource) {
        MessageUtil.messageSource = messageSource;
    }
    public static String get(String key) {
        return messageSource.getMessage(key, null, key, DEFAULT_LOCALE);
    }
    public static String get(String key, Object... params) {
        return messageSource.getMessage(key, params, key, DEFAULT_LOCALE);
    }
    public static String get(String key, Locale locale) {
        return messageSource.getMessage(key, null, locale);
    }
}
