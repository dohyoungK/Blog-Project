package gdh012.blog.global.auth.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginDto {
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식으로 입력되어야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{6,}$" , message = "영문, 숫자 포함 6글자 이상의 패스워드만 허용합니다.")
    private String password;
}
