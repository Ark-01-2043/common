package com.dnpa.common.util;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
@Component
public class MailUtil {
    @Value("${app.mail.enable:#{true}}")
    private boolean emailEnabled;
    @Value("${app.mail.smtp.host:#{'smtp.gmail.com'}}")
    private String smtpHost;
    @Value("${app.mail.smtp.port:#{587}}")
    private int smtpPort;
    @Value("${app.mail.smtp.auth:#{true}}")
    private boolean smtpAuth;
    @Value("${app.mail.smtp.user:#{'email@gmail.com'}}")
    private String smtpUser;
    @Value("${app.mail.smtp.password:#{'password'}}")
    private String smtpPassword;
    private Authenticator getSmtpAuthenticator() {
        return new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUser, smtpPassword);
            }
        };

    }
    private Session initSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.ssl.trust",  smtpHost);
        return Session.getDefaultInstance(props, getSmtpAuthenticator());
    }
    // TODO: add a sendmail method
}
