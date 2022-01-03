package com.example.sso.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http.authorizeRequests(a ->
                a.antMatchers("/", "/error", "/webjars/**").permitAll().anyRequest().authenticated()
        ).exceptionHandling(e -> e
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        ).logout(l -> l
                .logoutSuccessUrl("/").permitAll()
        ).csrf(c -> c
//                防止跨站请求伪造
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        )
                .oauth2Login();
    }
}
