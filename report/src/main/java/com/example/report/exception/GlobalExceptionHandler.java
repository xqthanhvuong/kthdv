package com.example.report.exception;

import com.example.report.dto.response.JsonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<JsonResponse<String>> handleRuntimeException(Exception e) {
//        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
//        return ResponseEntity.status(errorCode.getStatusCode()).body(JsonResponse.error(errorCode));
//    }

    @ExceptionHandler(value = BadException.class)
    ResponseEntity<JsonResponse<String>> handleAppException(BadException e) {
        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity.status(errorCode.getStatusCode()).body(JsonResponse.error(errorCode));
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<JsonResponse<String>> handleAccessDeniedException(AccessDeniedException e) {
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
        return ResponseEntity.status(errorCode.getStatusCode()).body(JsonResponse.error(errorCode));
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    ResponseEntity<JsonResponse<String>> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        return ResponseEntity.status(errorCode.getStatusCode()).body(JsonResponse.error(errorCode));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<JsonResponse<String>> handleArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "UNCATEGORIZED_EXCEPTION";
        ErrorCode errorCode = ErrorCode.getError(enumKey);
        return ResponseEntity.status(errorCode.getStatusCode()).body(JsonResponse.error(errorCode));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ex) {
        return ResponseEntity.status(ErrorCode.TOKEN_INVALID.getStatusCode()).body(JsonResponse.error(ErrorCode.TOKEN_INVALID));
    }
}

