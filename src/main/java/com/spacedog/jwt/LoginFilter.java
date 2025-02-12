package com.spacedog.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spacedog.member.domain.Refresh;
import com.spacedog.member.repository.RefreshRepository;
import com.spacedog.member.service.LoginRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;


@Slf4j
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;





    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        LoginRequest loginRequest = new LoginRequest();
        try {
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
             loginRequest = objectMapper.readValue(messageBody, LoginRequest.class);

        } catch (IOException e) {
//            throw new RuntimeException(e);

        }

        //클라이언트 요청에서 email, password 추출
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        log.info("email = {}, password = {}", email, password);


        //스프링 시큐리티에서 email, password 검증
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

        //토큰에 담은 값을 검증 하기위한 AuthenticationManager (검증 방법: DB에서 회원 정보 조회 후 UserDetailsService를 통해 유저 정보를 받고 검증 진행)
        return authenticationManager.authenticate(authToken);

    }


    // 로그인 검증 성공시 실행 하는 메서드 (여기서 JWT 토큰 발급)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authentication) throws IOException, ServletException {


        log.info("로그인 성공");

        //이메일 가져오기
        String email = authentication.getName();

        // role 가져오기
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 토큰 생성
        String access = jwtUtil.createJwt("Authorization", email, role, 86400000L); //하루
        String refresh = jwtUtil.createJwt("refresh", email, role, 86400000L);

        saveRefreshEntity(email, refresh, 86400000L);

        response.setHeader("Authorization", "Bearer " + access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());

        response.setCharacterEncoding("utf-8");
        response.getWriter().print("로그인이 성공 했습니다");

    }

    public void saveRefreshEntity(String email, String refresh, Long expiredMs) {

        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh entity = Refresh.builder()
                .email(email)
                .refreshToken(refresh)
                .expiration(date.toString())
                .build();
        refreshRepository.save(entity);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(86400000); //쿠키 생명주기
        cookie.setHttpOnly(true); // 클라이언트 단에서 js코드로 해당 쿠키를 접근 하지 못하게 하는 코드
//        cookie.setSecure(true); //https 통신 사용할경우
//        cookie.setPath("/"); 쿠키가 적용될 범위
        return cookie;

    }


    // 로그인 검증 실패시 실행하는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

    log.info("로그인 실패");
    response.setStatus(401);
    }


}
