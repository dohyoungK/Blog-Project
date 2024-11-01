package gdh012.blog.global.exception.response;

import gdh012.blog.global.exception.code.ExceptionCode;
import jakarta.validation.ConstraintViolation;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ErrorResponse {
    private final String field;
    private final Object invalidValue;
    private final String message;

    private ErrorResponse(String field, Object invalidValue, String message) {
        this.field = field;
        this.invalidValue = invalidValue;
        this.message = message;
    }

    private static ErrorResponse of(final String field, final Object invalidValue, final String message) {
        return new ErrorResponse(field, invalidValue, message);
    }

    public static List<ErrorResponse> of(final BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> ErrorResponse.of(
                        error.getField(),
                        error.getRejectedValue() == null ?
                                "" : error.getRejectedValue().toString(), // null이면 "", 아니면 오류난 값
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());
    }

    public static List<ErrorResponse> of(final Set<ConstraintViolation<?>> constraintViolations) {
        return constraintViolations.stream()
                .map(constraintViolation -> ErrorResponse.of(
                        constraintViolation.getPropertyPath().toString(),
                        constraintViolation.getInvalidValue().toString(),
                        constraintViolation.getMessage()
                ))
                .collect(Collectors.toList());
    }

    public static ErrorResponse of(final MissingServletRequestParameterException e) {
        return ErrorResponse.of(e.getParameterName(), null, e.getMessage());
    }

    public static ErrorResponse of(final MethodArgumentTypeMismatchException e) {
        return ErrorResponse.of(e.getName(), e.getValue(), e.getMessage());
    }

    public static ErrorResponse of(final HttpRequestMethodNotSupportedException e) {
        return ErrorResponse.of(Objects.requireNonNull(e.getSupportedMethods())[0], e.getMethod(), e.getMessage());
    }

    public static ErrorResponse of(final ExceptionCode code) {
        return ErrorResponse.of(null, null, code.getMessage());
    }
}