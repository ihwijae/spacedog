package com.spacedog.order.apicontroller;

import com.spacedog.global.ApiResponse;
import com.spacedog.member.domain.Member;
import com.spacedog.member.service.MemberService;
import com.spacedog.order.service.OrderCreateRequest;
import com.spacedog.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class OrderController {



    private final MemberService memberService;
    private final OrderService orderService;


    @PostMapping("/orders")
    public ApiResponse<Long> createOrder(@RequestBody OrderCreateRequest request) {
        Member member = memberService.getMember();

        Long result = orderService.createOrder(request, member);

        return ApiResponse.success(result, "주문 생성");
    }
}
