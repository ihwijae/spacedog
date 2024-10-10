package com.spacedog.global;

import lombok.Data;

@Data
public class ApiResponse<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String  ERROR_STATUS = "error";
    private static final String FAIL_STATUS = "fail";

    private String status;
    private String message;
    private T Data;

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        Data = data;
    }

    /**
     * 제네릭 메서드.
     * 매개변수 타입을 미리 정하지 않고 메서드를 호출할 때 타입을 지정하겠다는 의미다.
     * 즉, 제네릭 타입을 사용하여 어떤 타입이든 허용할 수있다 는 뜻
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(SUCCESS_STATUS, message, data);
    }

    public static ApiResponse<?> successNoResponse(String message){
        return new ApiResponse<>(SUCCESS_STATUS, message, null);
    }

    public static ApiResponse<?> successWithContent() {
        return new ApiResponse<>(SUCCESS_STATUS, null, null);
    }

    public static ApiResponse<?> errorResponse(String message) {
        return new ApiResponse<>(ERROR_STATUS, message, null);
    }



}
