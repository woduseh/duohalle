package com.hac.duohalle.infra.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Bean
    public static PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        http.authorizeRequests()
                .antMatchers("/js/**", "/css/**", "/img/**", "/fonts/**", "/vendor/**",
                        "/node_modules/**")
                .permitAll();

        http.authorizeRequests()
                .antMatchers("/", "/login", "/sign-up", "/sign-in", "/check-email-token",
                        "/check-email",
                        "/h2-console/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/sign-up/**", "/resend-confirm-email")
                .hasAnyAuthority("ROLE_USER")
                .mvcMatchers(HttpMethod.GET, "profile/*")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling();

        http.formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll();

        return http.build();
    }
}
