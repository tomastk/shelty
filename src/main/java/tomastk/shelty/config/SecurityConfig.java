package tomastk.shelty.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tomastk.shelty.jwtconfig.JwtAuthenticationFilter;
import tomastk.shelty.user.Role;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfig {
    private final AuthenticationProvider authProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    // Insert the public urls here:


    String[] publicUrls = {
            "/auth/login",
            "/auth/register",
            "/auth/valid-code",
            "/auth/forgot-password",
            "/public-info/**",
            "/docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authRequest ->
                authRequest
                .requestMatchers(publicUrls).permitAll()
                .requestMatchers("/admin/create").hasAuthority(Role.SUPER_ADMIN.name())
                .requestMatchers("/admin/**").hasAnyAuthority(Role.ADMIN.name(), Role.SUPER_ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(sessionManager -> {
                sessionManager
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            })
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .formLogin(Customizer.withDefaults())
                .build();
    }

}