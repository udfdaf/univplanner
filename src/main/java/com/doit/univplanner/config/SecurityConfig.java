package com.doit.univplanner.config;

import com.doit.univplanner.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/", "/login", "/signup", "/register", "/static/**", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/plans/**", "/courses/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .usernameParameter("username")    // 추가
                        .passwordParameter("password")    // 추가
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/plans/list", true)
                        .failureUrl("/login?error=true")  // 수정
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .userDetailsService(customUserDetailsService)  // 추가
                .logout((logout) -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                );

        return http.build();
    }
}