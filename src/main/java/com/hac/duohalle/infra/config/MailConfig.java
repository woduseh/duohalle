package com.hac.duohalle.infra.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    private static final JavaMailSender javaMailSender = new JavaMailSenderImpl();

    @Bean
    public JavaMailSender javaMailSender() {
        return javaMailSender;
    }
}
