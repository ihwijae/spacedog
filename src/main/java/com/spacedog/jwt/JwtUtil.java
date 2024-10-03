package com.spacedog.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    // application.yml 에서 설정한 암호화 키를 객체로 저장
    public JwtUtil(@Value("${jwt.secret}")String secret) {

        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts
                        .SIG.HS256
                        .key()
                        .build()
                        .getAlgorithm());
    }

    public String getUserEmail(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey) //토큰이 현재 서버에서 생성된 토큰인지, 현재 가지고 있는 토큰과 동일한지 검증
                .build()
                .parseSignedClaims(token)
                .getPayload() // 페이로드에서 특정한 데이터 추출
                .get("email", String.class); // 추출할 데이터 key,  타입
    }

    public String getRole(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    // 소멸되면 true, 소멸되지 않았으면 false
    public boolean isExpired(String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    // 토큰 비교 (Access 토큰이 필요한 요청인데 Refresh 토큰으로 요청을 하거나.. 그런 경우를 방지하기위함
    public String getCategory (String token) {
        return Jwts
                .parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("category", String.class);
    }



    public String createJwt (String category, String email, String role, Long expireMs) {

        return Jwts
                .builder()
                .claim("category", category)
                .claim("email", email) //payload에 해당하는 부분에 데이터를 넣는다.
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발행일자 (현재 시간) 밀리초 표현
                .expiration(new Date(System.currentTimeMillis() + expireMs)) //토큰 만료 시간
                .signWith(secretKey) //시크릿키를 통해 토큰 암호화 (시그니처 만들기)
                .compact();
    }

}
