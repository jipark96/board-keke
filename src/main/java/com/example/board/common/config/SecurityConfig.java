package com.example.board.common.config;

import com.example.board.user.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {


    private final UserDetailsServiceImpl userDetailsService;

//    //[시큐리티 기능 비활성화]
//    @Bean
//    public WebSecurityCustomizer configure() {
//        return (web -> web.ignoring()
//                .requestMatchers());
//
//    }

    //[특정 HTTP 요청에 대한 웹 기반 보안 구성]
    @Bean
   public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
       return http
               .cors()
               .and()
               .authorizeHttpRequests()
               .requestMatchers("/**").permitAll()
               .anyRequest().authenticated()
               .and()
               .csrf().disable()
               .build();
   }

   //[인증 관리자 관련 설정]
   @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() throws Exception {
       DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

       daoAuthenticationProvider.setUserDetailsService(userDetailsService);
       daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

       return daoAuthenticationProvider;
   }

   @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
       return new BCryptPasswordEncoder();
   }
}