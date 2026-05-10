package com.duoc.seguridadcalidad;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Passwords for in-memory users.
     * In production set USER_PASSWORD / ADMIN_PASSWORD env vars or
     * app.security.user-password / app.security.admin-password properties.
     */
    @Value("${app.security.user-password:}")
    private String userPassword;

    @Value("${app.security.admin-password:}")
    private String adminPassword;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/", "/home").permitAll()
                .requestMatchers("/login", "/api/auth/**").permitAll()
                .requestMatchers("/api/**").permitAll()
                // All Thymeleaf view pages — auth enforced client-side via JWT cookie
                .requestMatchers("/patients", "/patients/**").permitAll()
                .requestMatchers("/appointments", "/appointments/**").permitAll()
                .requestMatchers("/pets", "/pets/**").permitAll()
                .requestMatchers("/billing", "/billing/**").permitAll()
                // Static resources
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/auth.js", "/style.css", "/login.css").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .permitAll()
            )
            .logout(logout -> logout.permitAll())
            // Add Content-Security-Policy header to mitigate XSS (ZAP finding)
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self' 'unsafe-inline'; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "img-src 'self' data:; " +
                        "font-src 'self'; " +
                        "connect-src 'self'; " +
                        "frame-ancestors 'none'"
                    )
                )
            );

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Description("In-memory UserDetailsService — credentials sourced from properties or environment variables")
    UserDetailsService users() {
        // Prefer property value; fall back to env var
        String resolvedUserPass = (userPassword != null && !userPassword.isBlank())
                ? userPassword : System.getenv("USER_PASSWORD");
        String resolvedAdminPass = (adminPassword != null && !adminPassword.isBlank())
                ? adminPassword : System.getenv("ADMIN_PASSWORD");

        if (resolvedUserPass == null || resolvedUserPass.isBlank()
                || resolvedAdminPass == null || resolvedAdminPass.isBlank()) {
            throw new IllegalStateException(
                "Passwords must be set via app.security.user-password / app.security.admin-password " +
                "properties or USER_PASSWORD / ADMIN_PASSWORD environment variables");
        }

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode(resolvedUserPass))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode(resolvedAdminPass))
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
