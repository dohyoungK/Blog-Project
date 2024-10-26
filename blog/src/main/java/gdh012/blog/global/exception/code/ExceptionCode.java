package gdh012.blog.global.exception.code;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    LOGIN_FAILED(400, "Login Failed"),
    OAUTH2_LOGIN_FAILED(400, "OAuth2 Login Failed"),
    EXPIRED_TOKEN(401, "Expired Token"),
    INVALID_TOKEN(401, "Invalid Token"),

    INVALID_INPUT_VALUE(400, "Invalid Input Value"),
    PARAMETER_NOT_FOUND(400, "Parameter Not Found"),
    INVALID_INPUT_TYPE(400, "Invalid Input Type"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    ACCOUNT_UNAUTHORIZED(401, "Account Unauthorized"),
    ACCOUNT_ACCESS_DENIED(403, "Access is Denied"),
    INTERNAL_SERVER_ERROR(500, "Server Error"),

    ACCOUNT_NOT_FOUND(404, "Account Not Found"),
    ACCOUNT_NOT_ALLOW(405, "That Account doesn't have authority"),
    ACCOUNT_ALREADY_EXISTS(409, "Account Already Exists"),

    BOARD_NOT_FOUND(404, "Board Not Found"),
    BOARD_NOT_ALLOW(405, "That Board doesn't have authority"),
    BOARD_ALREADY_EXISTS(409, "Board Already Exists"),

    BOARD_IMAGE_NOT_FOUND(404, "Board Image Not Found"),
    BOARD_IMAGE_NOT_ALLOW(405, "That Board image doesn't have authority"),
    BOARD_IMAGE_ALREADY_EXISTS(409, "Board Image Already Exists"),

    COMMENT_NOT_FOUND(404, "Comment Not Found"),
    COMMENT_NOT_ALLOW(405, "That Comment doesn't have authority"),
    COMMENT_ALREADY_EXISTS(409, "Comment Already Exists");

    private final int status;
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
