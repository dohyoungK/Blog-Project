package gdh012.blog.global.exception.response;

import com.nimbusds.jose.shaded.gson.Gson;
import gdh012.blog.global.exception.code.BusinessLogicException;
import gdh012.blog.global.exception.code.ExceptionCode;
import gdh012.blog.global.response.SingleResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;

public class ErrorResponder {
    public static void sendErrorResponse(HttpServletResponse response, ExceptionCode exceptionCode) throws IOException {
        Gson gson = new Gson();
        SingleResponse<ErrorResponse> errorResponse =
                SingleResponse.fail(ErrorResponse.of(exceptionCode), new BusinessLogicException(exceptionCode));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(exceptionCode.getStatus());
        response.getWriter().write(gson.toJson(errorResponse, SingleResponse.class));
    }
}
