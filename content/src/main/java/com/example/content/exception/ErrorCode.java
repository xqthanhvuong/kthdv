package com.example.content.exception;


import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1000, "Uncategorized exception", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1001, "User already existed", HttpStatus.CONFLICT),
    USERNAME_INVALID(1002, "Invalid email format.", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1003,
            "Password must be 8-20 characters long and contain at least one lowercase letter, one uppercase letter, one digit, and one special character.",
            HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1004, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    NAME_INVALID(1006, "Name must be 2-50 characters long and not be blank", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED(1007, "You do not have permission.", HttpStatus.FORBIDDEN),
    METHOD_NOT_ALLOWED(1008, "Method not allowed.", HttpStatus.METHOD_NOT_ALLOWED),
    POST_NOT_EXISTED(1009, "Post not existed", HttpStatus.NOT_FOUND),
    COMMENT_INVALID(1010, "Comment invalid.", HttpStatus.BAD_REQUEST),
    MISSING_USER_ID(1011, "Missing user id", HttpStatus.BAD_REQUEST),
    MISSING_POST_ID(1012, "Missing post id", HttpStatus.BAD_REQUEST),
    POST_CONTENT_INVALID(1013, "Post content invalid", HttpStatus.BAD_REQUEST),
    POST_TITLE_INVALID(1014, "Post title invalid", HttpStatus.BAD_REQUEST),
    USER_IS_BLOCK(1015, "your account has been blocked.", HttpStatus.FORBIDDEN),
    INVALID_LOCATION(1016, "Invalid location", HttpStatus.BAD_REQUEST),
    COMMENT_NOT_EXISTED(1017, "Comment parent not existed", HttpStatus.NOT_FOUND),
    COMMENT_PARENT_INVALID(1018, "Comment parent invalid", HttpStatus.BAD_REQUEST),
    POST_IS_NOT_PUBLISH(1019, "This post is not publish", HttpStatus.BAD_REQUEST),
    TOKEN_INVALID(1020, "Token invalid", HttpStatus.UNAUTHORIZED),
    REJECTION_REASON_INVALID(1021, "The reason is not be blank, and max is 255 characters", HttpStatus.BAD_REQUEST),
    ;

    private static final Logger log = LoggerFactory.getLogger(ErrorCode.class);
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    // if err key wrong return code 1000
    public static ErrorCode getError(String errKey) {
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode = ErrorCode.valueOf(errKey);
        } catch (IllegalArgumentException ignored) {
            log.error("Error key wrong.");
        }
        return errorCode;
    }

}

