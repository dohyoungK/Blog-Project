package gdh012.blog.domain.account.constants;

import lombok.Getter;

public enum Role {
    GUEST(1, "ROLE_GUEST"),
    USER(2, "ROLE_USER");

    @Getter
    private int stepNumber;

    @Getter
    private String stepDescription;

    Role(int stepNumber, String stepDescription) {
        this.stepNumber = stepNumber;
        this.stepDescription = stepDescription;
    }
}
