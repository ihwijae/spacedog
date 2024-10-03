package com.spacedog.member.apicontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {



    @RequestMapping("/")
    public String home() {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Hello World!" + email;
    }

}
