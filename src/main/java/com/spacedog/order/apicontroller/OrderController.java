package com.spacedog.order.apicontroller;

import com.spacedog.global.ApiResponse;
import com.spacedog.order.dto.OrderDetailResponse;
import com.spacedog.order.impl.OrderCreateRequest;
import com.spacedog.order.service.OrderResponse;
import com.spacedog.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class OrderController {



    private final OrderService orderService;


    @PostMapping("/orders")
    public ApiResponse<Long> createOrder(@RequestBody OrderCreateRequest request) {

        Long result = orderService.createOrder(request);

        return ApiResponse.success(result, "주문 생성");
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getOrders();

        return ApiResponse.success(orders, "주문 전체 조회");
    }

    @GetMapping("/orders/{orderId}")
    public ApiResponse<OrderDetailResponse> getOrderDetail(@PathVariable("orderId") Long orderId) {
        log.info("orderId={}", orderId);
        OrderDetailResponse orderDetail = orderService.getOrderDetail(orderId);

        return ApiResponse.success(orderDetail, "주문 상세 조회");
    }
}
