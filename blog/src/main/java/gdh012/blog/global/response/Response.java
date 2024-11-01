package gdh012.blog.global.response;

import lombok.Getter;

@Getter
public class Response {
    private final int status;
    private final String code;
    private final String message;

    public Response(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
