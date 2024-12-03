package gdh012.blog.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Post {
        @NotBlank(message = "제목은 필수입니다.")
        private String title;

        @NotBlank(message = "본문은 필수입니다.")
        private String content;

        public Post(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Update {
        @NotNull
        private String title;

        @NotNull
        private String content;

        public Update(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }

    @Getter
    @Builder
    public static class Response {
        private final Long boardId;
        private final String title;
        private final String content;

        public Response(Long boardId, String title, String content) {
            this.boardId = boardId;
            this.title = title;
            this.content = content;
        }
    }
}
