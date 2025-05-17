package service.authservice.utils;

import org.springframework.http.ResponseEntity;
import service.authservice.entity.response.ApiResponse;

@SuppressWarnings("all")
public class ResponseBuilder {
    public static <T> ResponseEntity<ApiResponse<T>> buildSuccessResponse(String message, T data) {
        return ResponseEntity.ok(ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildBadRequestResponse(T errorMessage) {
        return ResponseEntity.badRequest().body(ApiResponse.<T>builder()
                .success(false)
                .message(errorMessage.toString())
                .build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> buildServerErrorResponse(T errorMessage) {
        return ResponseEntity.internalServerError().body(ApiResponse.<T>builder()
                .success(false)
                .message(errorMessage.toString())
                .build());
    }
}
