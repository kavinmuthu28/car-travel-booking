package com.cartravel.booking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) { return buildError(HttpStatus.NOT_FOUND, ex.getMessage()); }
    @ExceptionHandler({UserNotFoundException.class, CarNotFoundException.class, BookingNotFoundException.class, PaymentNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleEntityNotFound(RuntimeException ex) { return buildError(HttpStatus.NOT_FOUND, ex.getMessage()); }
    @ExceptionHandler({CarUnavailableException.class, InvalidBookingException.class})
    public ResponseEntity<Map<String, Object>> handleBookingConflicts(RuntimeException ex) { return buildError(HttpStatus.CONFLICT, ex.getMessage()); }
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateResourceException ex) { return buildError(HttpStatus.CONFLICT, ex.getMessage()); }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) { return buildError(HttpStatus.UNAUTHORIZED, "Invalid email or password."); }
    @ExceptionHandler({UnauthorizedException.class, AccessDeniedException.class})
    public ResponseEntity<Map<String, Object>> handleAccessDenied(Exception ex) { return buildError(HttpStatus.FORBIDDEN, "Access denied."); }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) { errors.put(fieldError.getField(), fieldError.getDefaultMessage()); }
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now()); body.put("status", HttpStatus.BAD_REQUEST.value()); body.put("error", "Validation Failed"); body.put("fieldErrors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) { return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error: " + ex.getMessage()); }
    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String msg) {
        Map<String, Object> body = new HashMap<>(); body.put("timestamp", LocalDateTime.now()); body.put("status", status.value()); body.put("error", status.getReasonPhrase()); body.put("message", msg);
        return new ResponseEntity<>(body, status);
    }
}