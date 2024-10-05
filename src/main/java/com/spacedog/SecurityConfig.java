package com.spacedog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spacedog.jwt.CustomLogoutFilter;
import com.spacedog.jwt.JwtFilter;
import com.spacedog.jwt.JwtUtil;
import com.spacedog.jwt.LoginFilter;
import com.spacedog.member.repository.MemberRepository;
import com.spacedog.member.repository.RefreshRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final AuthenticationConfiguration authenticationConfiguration;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;


    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MemberRepository memberRepository) throws Exception {

        // cors 설정
        http
                .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 프론트엔드 서버 포트
                        configuration.setAllowedMethods(Collections.singletonList("*")); //허용할 Http 메서드
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*")); //허용할 Http 헤더
                        configuration.setMaxAge(3600L); //허용을 물고있을 시간
                        configuration.setExposedHeaders(Collections.singletonList("Authorization")); // 백엔드 -> 프론트로 응답 해줄때 헤더에 Authorization에 Jwt를 보내주니까 허용해줘야한다.

                        return configuration;
                    }
                }));

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/api/join", "/reissue").permitAll() // 모든 권한을 허용
                        .requestMatchers("/admin").hasRole("ADMIN") // ADMIN 권한 사용자만 접근
                        .anyRequest().authenticated()); // 다른 요청에 대해서는 로그인한 사용자만 접근 가능

//        // 모든 요청에 대해 접근 허용
//        http
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll() // 모든 요청에 대해 접근 허용
//                );

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // session을 무상태로 설정하는것 (스테이트리스)


        // LoginFilter (필터) 추가 - 스프링 시큐리티 필터체인에 필터를 등록
        http
                .addFilterAt(new LoginFilter(authenticationManagerBean(authenticationConfiguration), objectMapper, jwtUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class);

        //JwtFilter 필터 등록
        http
                .addFilterBefore(new JwtFilter(jwtUtil, memberRepository), LoginFilter.class); //로그인 필터 앞에 먼저 실행한다는 뜻.

        http
                .addFilterBefore(new CustomLogoutFilter(refreshRepository, jwtUtil), LogoutFilter.class);



        return http.build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




}
