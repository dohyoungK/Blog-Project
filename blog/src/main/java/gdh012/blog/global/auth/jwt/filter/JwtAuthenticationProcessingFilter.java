package gdh012.blog.global.auth.jwt.filter;

import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.domain.account.repository.AccountRepository;
import gdh012.blog.global.auth.jwt.service.JwtService;
import gdh012.blog.global.exception.code.BusinessLogicException;
import gdh012.blog.global.exception.code.ExceptionCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AccountRepository accountRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String authorization = request.getHeader("Authorization");

        // authorization이 null이거나 Bearer로 시작하지 않으면 이 필터를 실행하지 않는다.(shouldNotFilter)
        return authorization == null || !authorization.startsWith("Bearer");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String refreshToken = jwtService.extractRefreshToken(request)
                .orElse(null);

        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        } else {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
        }
    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.extractAccessToken(request).orElse(null);
        jwtService.verifyToken(accessToken);

        Account findAccount = accountRepository.findByEmail(jwtService.extractEmail(accessToken).orElse(null))
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ACCOUNT_NOT_FOUND));

        saveAuthentication(findAccount);
        filterChain.doFilter(request, response);
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        accountRepository.findByRefreshToken(refreshToken)
                .ifPresent(account -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(account);
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(account.getEmail()), reIssuedRefreshToken);
                });
    }

    private String reIssueRefreshToken(Account account) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        account.updateRefreshToken(reIssuedRefreshToken);
        accountRepository.saveAndFlush(account);
        return reIssuedRefreshToken;
    }

    public void saveAuthentication(Account account) {
        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .roles(account.getRole().name())
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
