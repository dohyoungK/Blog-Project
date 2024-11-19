package gdh012.blog.domain.account.service;

import gdh012.blog.domain.account.constants.Role;
import gdh012.blog.domain.account.dto.AccountDto;
import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.domain.account.repository.AccountRepository;
import gdh012.blog.global.exception.code.BusinessLogicException;
import gdh012.blog.global.exception.code.ExceptionCode;
import gdh012.blog.global.utils.AuthUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserUtil authUserUtil;

    public AccountDto.Response signUp(AccountDto.SignUp signUpDto) {
        verifyEmail(signUpDto.getEmail());
        verifyNickname(signUpDto.getNickname());

        Account account = Account.builder()
                .email(signUpDto.getEmail())
                .nickname(signUpDto.getNickname())
                .password(signUpDto.getPassword())
                .role(Role.USER)
                .build();

        account.encodePassword(passwordEncoder);
        account = accountRepository.save(account);

        return AccountDto.Response.builder()
                .accountId(account.getAccountId())
                .build();
    }

    public AccountDto.Response getAccount() {
        Account findAccount = authUserUtil.getAuthUser();

        return AccountDto.Response.builder()
                .accountId(findAccount.getAccountId())
                .email(findAccount.getEmail())
                .nickname(findAccount.getNickname())
                .build();
    }

    public void updateAccount(AccountDto.Update updateDto) {
        Account findAccount = authUserUtil.getAuthUser();
        String changedNickname = updateDto.getNickname();
        String changedPassword = updateDto.getPassword();

        if (!changedNickname.equals("")) verifyNickname(changedNickname);
        if (!changedPassword.equals("")) verifyPassword(changedPassword);

        findAccount.updateAccount(changedNickname, changedPassword);
        findAccount.encodePassword(passwordEncoder);
    }

    public void deleteAccount() {
        Account findAccount = authUserUtil.getAuthUser();

        accountRepository.delete(findAccount);
    }

    private void verifyEmail(String email) {
        if (accountRepository.findByEmail(email).isPresent()) {
            throw new BusinessLogicException(ExceptionCode.EMAIL_ALREADY_EXISTS);
        }
    }

    private void verifyNickname(String nickname) {
        if (accountRepository.findByNickname(nickname).isPresent()) {
            throw new BusinessLogicException(ExceptionCode.NICKNAME_ALREADY_EXISTS);
        }
    }

    private void verifyPassword(String password) {
        if (!password.matches("^(?=.*[a-zA-Z])(?=.*\\d).{6,}$")) {
            throw new BusinessLogicException(ExceptionCode.PASSWORD_NOT_VALID);
        }
    }
}
