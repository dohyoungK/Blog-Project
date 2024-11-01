package gdh012.blog.global.response;

import gdh012.blog.global.exception.code.BusinessLogicException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class MultiResponse<T> extends Response {
    private final List<T> data;

    private MultiResponse(List<T> data, int status, String code, String message) {
        super(status, code, message);
        this.data = data;
    }

    private static <T> MultiResponse<T> of(List<T> data, HttpStatus httpStatus) {
        return MultiResponse.of(data, httpStatus.value(), httpStatus.name(), httpStatus.getReasonPhrase());
    }

    private static <T> MultiResponse<T> of(List<T> data, int status, String code, String message) {
        return new MultiResponse<>(data, status, code, message);
    }

    public static <T> MultiResponse<T> success(List<T> data) {
        return MultiResponse.of(data, HttpStatus.OK);
    }

    public static <T> MultiResponse<T> success() {
        return MultiResponse.of(null, HttpStatus.OK);
    }

    public static <T> MultiResponse<T> fail(List<T> data, BusinessLogicException e) {
        return new MultiResponse<>(
                data,
                e.getExceptionCode().getStatus(),
                e.getExceptionCode().getCode(),
                e.getExceptionCode().getMessage());
    }
}
