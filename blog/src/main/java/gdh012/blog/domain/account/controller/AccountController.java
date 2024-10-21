package gdh012.blog.domain.account.controller;

import gdh012.blog.domain.account.dto.AccountDto;
import gdh012.blog.domain.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RequestMapping("/account")
@RestController
public class AccountController {
    private final AccountService accountService;

    @PostMapping("/signUp")
    public String signUp(@Valid @RequestBody AccountDto.SignUp accountSignUpDto) throws Exception {
        accountService.signUp(accountSignUpDto);

        return "signup";
    }

    @GetMapping("/jwtTest")
    public String jwtTest() {
        return "jwt 성공";
    }
}
