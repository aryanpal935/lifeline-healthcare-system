package com.lifeline.lifeline.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.lifeline.lifeline.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    // Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // Authentication Provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(
                customUserDetailsService
        );

        authProvider.setPasswordEncoder(
                passwordEncoder()
        );

        return authProvider;
    }

    // Security Configuration
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                    .requestMatchers(
                            "/register",
                            "/login",
                            "/css/**",
                            "/js/**",
                            "/images/**"
                    ).permitAll()

                    .requestMatchers("/admin/**")
                    .hasRole("ADMIN")

                    .requestMatchers("/dashboard/**")
                    .hasAnyRole("USER", "ADMIN")

                    .anyRequest()
                    .authenticated()
            )

            .formLogin(form -> form

                    .loginPage("/login")

                    .loginProcessingUrl("/login")

                    .defaultSuccessUrl(
                            "/dashboard",
                            true
                    )

                    .failureUrl("/login?error")

                    .permitAll()
            )

            .logout(logout -> logout

                    .logoutSuccessUrl("/login?logout")

                    .permitAll()
            );

        return http.build();
    }
}