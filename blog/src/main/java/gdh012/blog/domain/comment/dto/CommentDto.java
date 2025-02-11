package gdh012.blog.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommentDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Request {
        @NotBlank(message = "댓글 내용은 필수입니다.")
        private String content;

        public Request(String content) {
            this.content = content;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Update {
        @NotBlank(message = "댓글 내용은 필수입니다.")
        private String content;

        public Update(String content) {
            this.content = content;
        }
    }

    @Getter
    @Builder
    public static class Response {
        private final Long commentId;
        private final String content;

        public Response(Long commentId, String content) {
            this.commentId = commentId;
            this.content = content;
        }
    }
}
