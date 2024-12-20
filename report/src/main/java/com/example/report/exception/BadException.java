package com.example.report.exception;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BadException extends RuntimeException {
    private ErrorCode errorCode;

    public BadException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
