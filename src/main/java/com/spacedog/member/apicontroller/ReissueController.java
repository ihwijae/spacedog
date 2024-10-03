package com.spacedog.member.apicontroller;

import com.spacedog.jwt.ReissueService;
import com.spacedog.jwt.TokenResponseDto;
import com.spacedog.member.repository.RefreshRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReissueController {

    /**
     * Refresh 토큰을 받고 Access 토큰을 재발급 하는 컨트롤러
     */

    private final ReissueService reissueService;
    private final RefreshRepository refreshRepository;


    @PostMapping("/reissue")
    public ResponseEntity<String> reissue (@CookieValue(name = "refresh", required = false) String refresh, HttpServletResponse response) {

        log.info("cookie = {}", refresh);
        TokenResponseDto result = reissueService.reissue(refresh);

            Cookie cookie = reissueService.createCookie("refresh", result.getRefreshToken());
            log.info("cookie = {}", cookie);
            response.setHeader("Authorization", "Bearer " + result.getAccessToken());
            response.addCookie(cookie);
            return new ResponseEntity<>("새로운 access토큰 발급", HttpStatus.OK);

    }
}
