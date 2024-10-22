package gdh012.blog.global.jwt.handler;

import com.nimbusds.jose.shaded.gson.Gson;
import gdh012.blog.global.exception.code.ExceptionCode;
import gdh012.blog.global.exception.response.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Gson gson = new Gson();
        ErrorResponse errorResponse = ErrorResponse.of(ExceptionCode.ACCOUNT_ACCESS_DENIED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(gson.toJson(errorResponse, ErrorResponse.class));
    }
}
