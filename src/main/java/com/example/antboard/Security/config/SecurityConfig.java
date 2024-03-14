package com.example.antboard.Security.config;

import com.example.antboard.Security.jwt.JWTFilter;
import com.example.antboard.Security.jwt.JwtAuthenticationEntryPoint;
import com.example.antboard.Security.jwt.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JWTFilter jwtFilter;
    private final CorsConfigurationSource corsConfigurationSource;


    public SecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JWTFilter jwtFilter, JwtTokenUtil jwtUtil, CorsConfig config, CorsConfigurationSource corsConfigurationSource) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtFilter = jwtFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf disable
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource));


        //From 로그인 방식 disable
        http
                .formLogin(AbstractHttpConfigurer::disable);

        //http basic 인증 방식 disable
        http
                .httpBasic(AbstractHttpConfigurer::disable);

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .anyRequest().authenticated());

        http
                .exceptionHandling(except -> except.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
