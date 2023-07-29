package br.com.valdircezar.authserviceapi.controllers.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import models.exceptions.RefreshTokenExpired;
import models.exceptions.StandardError;
import models.exceptions.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler({BadCredentialsException.class, RefreshTokenExpired.class})
    ResponseEntity<StandardError> handleBadCredentialsException(
            final RuntimeException ex, final HttpServletRequest request
    ) {
        return ResponseEntity.status(UNAUTHORIZED).body(
                StandardError.builder()
                        .timestamp(now())
                        .status(UNAUTHORIZED.value())
                        .error(UNAUTHORIZED.getReasonPhrase())
                        .message(ex.getMessage())
                        .path(request.getRequestURI())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<StandardError> handleMethodArgumentNotValidException(
            final MethodArgumentNotValidException ex, final HttpServletRequest request
    ) {
        var error = ValidationException.builder()
                .timestamp(now())
                .status(BAD_REQUEST.value())
                .error("Validation Exception")
                .message("Exception in validation attributes")
                .path(request.getRequestURI())
                .errors(new ArrayList<>())
                .build();

        for(FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            error.addError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(error);
    }
}
