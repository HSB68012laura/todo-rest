package com.dwes.todo_rest.security;

import com.dwes.todo_rest.error.CustomAccessDeniedHandler;
import com.dwes.todo_rest.error.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(excep -> {
                    excep.authenticationEntryPoint(authenticationEntryPoint);
                    excep.accessDeniedHandler(accessDeniedHandler);

                })
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST,"/auth/register").permitAll()
                        //.requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated()
                );


        http.csrf((csrf) -> {
            csrf.disable();
        });
        http.headers((headers) ->
                headers.frameOptions((opts) -> opts.disable()));
        return http.build();
    }


}
