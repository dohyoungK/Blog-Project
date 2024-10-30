package gdh012.blog.global.logging.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MDCLoggingFilter extends OncePerRequestFilter {
    // 보통 MDC는 필터나 인터셉터에 적용하는 데 가장 앞 단인 필터에 적용하는 것이 좋다
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final UUID uuid = UUID.randomUUID();
        MDC.put("UUID", uuid.toString());
        filterChain.doFilter(request, response);
        MDC.clear();
    }
}
