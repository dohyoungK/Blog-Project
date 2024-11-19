package gdh012.blog.domain.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AccountDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class SignUp {
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식으로 입력되어야 합니다.")
        private String email;

        @NotBlank(message = "닉네임은 필수입니다.")
        private String nickname;

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).{6,}$" , message = "영문, 숫자 포함 6글자 이상의 패스워드만 허용합니다.")
        private String password;

        public SignUp(String email, String nickname, String password) {
            this.email = email;
            this.nickname = nickname;
            this.password = password;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Update {
        @NotNull
        private String nickname;

        @NotNull
        private String password;

        public Update(String nickname, String password) {
            this.nickname = nickname;
            this.password = password;
        }
    }

    @Getter
    @Builder
    public static class Response {
        private final Long accountId;
        private final String email;
        private final String nickname;

        public Response(Long accountId, String email, String nickname) {
            this.accountId = accountId;
            this.email = email;
            this.nickname = nickname;
        }
    }
}
