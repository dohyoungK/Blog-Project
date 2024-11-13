package gdh012.blog.global.utils;

import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.domain.account.repository.AccountRepository;
import gdh012.blog.global.exception.code.BusinessLogicException;
import gdh012.blog.global.exception.code.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AuthUserUtil {
    private final AccountRepository accountRepository;

    public Account getAuthUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String name = authentication.getName();

        if (name == null || name.equals("anonymousUser")) {
            throw new BusinessLogicException(ExceptionCode.ACCOUNT_UNAUTHORIZED);
        }

        return accountRepository.findByEmail(name).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.ACCOUNT_NOT_FOUND));
    }
}
