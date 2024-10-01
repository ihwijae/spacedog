package com.jwtmember.jwt;

import com.jwtmember.member.domain.Refresh;
import com.jwtmember.exception.RefreshTokenException;
import com.jwtmember.exception.RefreshTokenException.RefreshTokenExpiredException;
import com.jwtmember.member.repository.RefreshRepository;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.jwtmember.exception.RefreshTokenException.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReissueService {

    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;


    public TokenResponseDto reissue(String refresh) {

        log.info("reissue refresh: {}", refresh);

        // 쿠키가 있는지 확인
        if (refresh == null) {
            throw new RefreshTokenNullException("토큰이 존재하지 않습니다");
        }

        // 토큰 만료 체크
        try {
            jwtUtil.isExpired(refresh);
        } catch (RefreshTokenExpiredException e) {

            throw new RefreshTokenExpiredException("Refresh 토큰이 만료 됐습니다");
        }

        String category = jwtUtil.getCategory(refresh);

        // refresh 토큰인지 확인
        if(!category.equals("refresh")) {
            throw new RefreshTokenInvalidException("Refresh 토큰이 아닙니다");
        }

        //DB에 저장 되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefreshToken(refresh);

        if (!isExist) {
            throw new RefreshTokenException.RefreshTokenDataBase("DB에 토큰이 존재하지 않습니다");
        }


        String userEmail = jwtUtil.getUserEmail(refresh);
        String role = jwtUtil.getRole(refresh);


        // 새로운 access 토큰 발급
        String newAccessToken = jwtUtil.createJwt("Authorization", userEmail, role, 600000L);
        String newRefreshToken = jwtUtil.createJwt("refresh", userEmail, role, 86400000L);

        // DB에 존재하는 기존 Refresh 토큰을 삭제하고 새로운 토큰을 저장
        refreshRepository.deleteByRefreshToken(refresh);
        saveRefreshEntity(userEmail, newRefreshToken, 86400000L);

        return new TokenResponseDto(newAccessToken, newRefreshToken);
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


    public  Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(86400000); //쿠키 생명주기
        cookie.setHttpOnly(true); // 클라이언트 단에서 js코드로 해당 쿠키를 접근 하지 못하게 하는 코드
//        cookie.setSecure(true); //https 통신 사용할경우
//        cookie.setPath("/"); 쿠키가 적용될 범위
        return cookie;

    }
}





