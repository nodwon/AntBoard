//package com.example.antboard.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.web.cors.CorsConfigurationSource;
//import org.springframework.security.web.SecurityFilterChain;
//
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        return http
//                .httpBasic(httpBasic -> httpBasic.disable())
//                .csrf(csrf -> csrf.disable())
//                .cors(cors -> cors.configurationSource(corsConfigurationSource))
//                .build();
//    }
//}
