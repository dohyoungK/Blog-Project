package gdh012.blog.global.response;

import gdh012.blog.global.exception.code.BusinessLogicException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SingleResponse<T> extends Response {
    private final T data;

    private SingleResponse(T data, int status, String code, String message) {
        super(status, code, message);
        this.data = data;
    }

    private static <T> SingleResponse<T> of(T data, HttpStatus httpStatus) {
        return SingleResponse.of(data, httpStatus.value(), httpStatus.name(), httpStatus.getReasonPhrase());
    }

    private static <T> SingleResponse<T> of(T data, int status, String code, String message) {
        return new SingleResponse<>(data, status, code, message);
    }

    public static <T> SingleResponse<T> success(T data) {
        return SingleResponse.of(data, HttpStatus.OK);
    }

    public static <T> SingleResponse<T> success() {
        return SingleResponse.of(null, HttpStatus.OK);
    }

   public static <T> SingleResponse<T> fail(T data, BusinessLogicException e) {
        return SingleResponse.of(
                data,
                e.getExceptionCode().getStatus(),
                e.getExceptionCode().getCode(),
                e.getExceptionCode().getMessage());
    }
}
