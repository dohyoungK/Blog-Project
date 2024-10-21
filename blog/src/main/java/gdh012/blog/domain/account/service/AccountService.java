package gdh012.blog.domain.account.service;

import gdh012.blog.domain.account.constants.Role;
import gdh012.blog.domain.account.dto.AccountDto;
import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.domain.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(AccountDto.SignUp signUpDto) throws Exception {
        if (accountRepository.findByEmail(signUpDto.getEmail()).isPresent()) {
            throw new Exception("이미 존재하는 이메일");
        }

        if (accountRepository.findByNickname(signUpDto.getNickname()).isPresent()) {
            throw new Exception("이미 존재하는 닉네임");
        }

        Account account = Account.builder()
                .email(signUpDto.getEmail())
                .nickname(signUpDto.getNickname())
                .password(signUpDto.getPassword())
                .role(Role.USER)
                .build();

        account.encodePassword(passwordEncoder);
        accountRepository.save(account);
    }

//    public boolean validateAccount() {
//
//    }
}
