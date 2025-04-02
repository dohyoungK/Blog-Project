package gdh012.blog.domain.tag.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TagDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Request {
        @NotBlank(message = "태그 이름은 필수입니다.")
        private String name;

        public Request(String name) {
            this.name = name;
        }
    }

    @Getter
    @Builder
    public static class Response {
        private final Long tagId;
        private final String name;

        public Response(Long tagId, String name) {
            this.tagId = tagId;
            this.name = name;
        }
    }
}
