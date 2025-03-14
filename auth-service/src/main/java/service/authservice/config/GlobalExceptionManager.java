package service.authservice.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.paseto4j.commons.PasetoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionManager {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("IOException occurred");
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> handleSQLException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SQLException occurred");
    }

    @ExceptionHandler(PasetoException.class)
    public ResponseEntity<String> handlePasetoException(PasetoException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleJsonProcessingException(JsonProcessingException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
