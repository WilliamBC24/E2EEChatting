package service.authservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.paseto4j.commons.PasetoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import service.authservice.entity.response.ApiResponse;
import service.authservice.utils.ResponseBuilder;

import java.io.IOException;
import java.sql.SQLException;

@RestControllerAdvice
public class GlobalExceptionManager {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<String>> handleException(Exception e) {
        return ResponseBuilder.buildServerErrorResponse(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseBuilder.buildBadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<String>> handleIllegalStateException(IllegalStateException e) {
        return ResponseBuilder.buildBadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<String>> handleValidationExceptions(MethodArgumentNotValidException e) {
        return ResponseBuilder.buildBadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiResponse<String>> handleIOException(IOException e) {
        return ResponseBuilder.buildBadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiResponse<String>> handleSQLException(SQLException e) {
        return ResponseBuilder.buildBadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(PasetoException.class)
    public ResponseEntity<ApiResponse<String>> handlePasetoException(PasetoException e) {
        return ResponseBuilder.buildBadRequestResponse(e.getMessage());
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ApiResponse<String>> handleJsonProcessingException(JsonProcessingException e) {
        return ResponseBuilder.buildBadRequestResponse(e.getMessage());
    }
}
