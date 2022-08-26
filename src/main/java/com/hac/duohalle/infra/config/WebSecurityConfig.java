package com.hac.duohalle.infra.config;

import com.hac.duohalle.domain.account.service.AccountService;
import javax.sql.DataSource;
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
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AccountService accountService;

    private final DataSource dataSource;

    @Bean
    public static PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)
            throws Exception {
        // resources
        http.authorizeRequests()
                .antMatchers("/js/**", "/css/**", "/img/**", "/fonts/**", "/vendor/**",
                        "/node_modules/**")
                .permitAll();

        // H2 Console
        http.authorizeRequests()
                .antMatchers("/h2-console/**")
                .permitAll();

        // login
        http.authorizeRequests()
                .antMatchers("/", "/login", "/sign-up", "/sign-in", "/check-email-token",
                        "/check-email")
                .permitAll();

        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/sign-up/**", "/resend-confirm-email",
                        "/profile/setting/**")
                .hasAnyAuthority("ROLE_USER")
                .mvcMatchers(HttpMethod.GET, "/profile/*")
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

        http.rememberMe()
                .userDetailsService(accountService)
                .tokenRepository(tokenRepository());

        return http.build();
    }

    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}
