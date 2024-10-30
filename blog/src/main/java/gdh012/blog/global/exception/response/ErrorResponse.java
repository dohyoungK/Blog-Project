package gdh012.blog.global.exception.response;

import gdh012.blog.global.exception.code.ExceptionCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private int status;
    private String message;
    private String path;
    private List<Error> errors;

    public static ErrorResponse of(final ExceptionCode code, final BindingResult bindingResult,
                                   final HttpServletRequest httpServletRequest) {
        return ErrorResponse.builder()
                .status(code.getStatus())
                .message(code.getMessage())
                .path(httpServletRequest.getRequestURI())
                .errors(Error.of(bindingResult))
                .build();
    }

    public static ErrorResponse of(final ExceptionCode code, final Set<ConstraintViolation<?>> violations,
                                   final HttpServletRequest httpServletRequest) {
        return ErrorResponse.builder()
                .status(code.getStatus())
                .message(code.getMessage())
                .path(httpServletRequest.getRequestURI())
                .errors(Error.of(violations))
                .build();
    }

    public static ErrorResponse of(final ExceptionCode code, final MissingServletRequestParameterException e,
                                   final HttpServletRequest httpServletRequest) {
        return ErrorResponse.builder()
                .status(code.getStatus())
                .message(code.getMessage())
                .path(httpServletRequest.getRequestURI())
                .errors(Error.of(e.getParameterName(), null, e.getMessage()))
                .build();
    }

    public static ErrorResponse of(final ExceptionCode code, final MethodArgumentTypeMismatchException e,
                                   final HttpServletRequest httpServletRequest) {
        return ErrorResponse.builder()
                .status(code.getStatus())
                .message(code.getMessage())
                .path(httpServletRequest.getRequestURI())
                .errors(Error.of(e.getName(), e.getValue(), e.getMessage()))
                .build();
    }

    public static ErrorResponse of(final ExceptionCode code, final HttpRequestMethodNotSupportedException e,
                                   final HttpServletRequest httpServletRequest) {
        return ErrorResponse.builder()
                .status(code.getStatus())
                .message(code.getMessage())
                .path(httpServletRequest.getRequestURI())
                .errors(Error.of(e.getSupportedMethods()[0], e.getMethod(), e.getMessage()))
                .build();
    }

    public static ErrorResponse of(final ExceptionCode code) {
        return ErrorResponse.builder()
                .status(code.getStatus())
                .message(code.getMessage())
                .build();
    }

    public static ErrorResponse of(final ExceptionCode exceptionCode, final HttpServletRequest httpServletRequest) {
        return ErrorResponse.builder()
                .status(exceptionCode.getStatus())
                .message(exceptionCode.getMessage())
                .path(httpServletRequest.getRequestURI())
                .build();
    }

    @Getter
    public static class Error {
        private final String field;
        private final Object invalidValue;
        private final String message;

        private Error(String message) {
            this.field = null;
            this.invalidValue = null;
            this.message = message;
        }

        private Error(String field, Object invalidValue, String message) {
            this.field = field;
            this.invalidValue = invalidValue;
            this.message = message;
        }

        public static List<Error> of(final String message) {
            List<Error> errors = new ArrayList<>();
            errors.add(new Error(message));
            return errors;
        }

        public static List<Error> of(final String field, final Object invalidValue, final String message) {
            List<Error> errors = new ArrayList<>();
            errors.add(new Error(field, invalidValue, message));
            return errors;
        }

        public static List<Error> of(final BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(error -> new Error(
                            error.getField(),
                            error.getRejectedValue() == null ?
                                    "" : error.getRejectedValue().toString(), // null이면 "", 아니면 오류난 값
                            error.getDefaultMessage()
                    )).collect(Collectors.toList());
        }

        public static List<Error> of(final Set<ConstraintViolation<?>> constraintViolations) {
            return constraintViolations.stream()
                    .map(constraintViolation -> new Error(
                            constraintViolation.getPropertyPath().toString(),
                            constraintViolation.getInvalidValue().toString(),
                            constraintViolation.getMessage()
                    )).collect(Collectors.toList());
        }
    }
}