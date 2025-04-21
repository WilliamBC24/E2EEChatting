package service.authservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.paseto4j.commons.PasetoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import service.authservice.entity.response.ApiResponse;

import java.io.IOException;
import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionManager {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.<String>builder()
                .success(false)
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                .success(false)
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                .success(false)
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.<String>builder()
                .success(false)
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<String>> handleIOException(IOException e) {
        return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                .success(false)
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse<String>> handleSQLException(SQLException e) {
        return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                .success(false)
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(PasetoException.class)
    public ResponseEntity<ApiResponse<String>> handlePasetoException(PasetoException e) {
        return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                .success(false)
                .message(e.getMessage())
                .build());
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ApiResponse<String>> handleJsonProcessingException(JsonProcessingException e) {
        return ResponseEntity.badRequest().body(ApiResponse.<String>builder()
                .success(false)
                .message(e.getMessage())
                .build());
    }
}
