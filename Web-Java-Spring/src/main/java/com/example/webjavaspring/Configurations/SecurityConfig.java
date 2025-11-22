package com.example.webjavaspring.Configurations;

import com.example.webjavaspring.Service.CustomerUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomerUserDetailsService userDetailsService;

    public SecurityConfig(CustomerUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/api/events/create", "/api/events/customers/**").hasAuthority("ROLE_ADMIN")

                        .requestMatchers("/api/events/buy/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                        .requestMatchers("/", "/api/events", "/api/events/search/**", "/api/events/login", "/api/register", "/css/**").permitAll()

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/api/events/login")
                        .defaultSuccessUrl("/api/events", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/api/events/logout")
                        .logoutSuccessUrl("/api/events")
                        .permitAll()
                );

        return http.build();
    }
}