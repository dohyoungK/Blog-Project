package gdh012.blog.domain.account.controller;

import gdh012.blog.domain.account.dto.AccountDto;
import gdh012.blog.domain.account.service.AccountService;
import gdh012.blog.global.response.SingleResponse;
import gdh012.blog.global.utils.UriCreator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Validated
@RequiredArgsConstructor
@RequestMapping("/accounts")
@RestController
public class AccountController {
    private static final String ACCOUNT_DEFAULT_URL = "/accounts";

    private final AccountService accountService;

    @PostMapping("/signUp")
    public ResponseEntity<HttpStatus> signUp(@Valid @RequestBody AccountDto.SignUp signUpDto) {
        AccountDto.Response responseDto = accountService.signUp(signUpDto);
        URI location = UriCreator.createUri(ACCOUNT_DEFAULT_URL, responseDto.getAccountId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<SingleResponse<AccountDto.Response>> getAccount() {
        AccountDto.Response responseDto = accountService.getAccount();

        return ResponseEntity.ok(SingleResponse.success(responseDto));
    }

    @PatchMapping("/update")
    public ResponseEntity<HttpStatus> updateAccount(@Valid @RequestBody AccountDto.Update updateDto) {
        accountService.updateAccount(updateDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAccount() {
        accountService.deleteAccount();

        return ResponseEntity.noContent().build();
    }
}
