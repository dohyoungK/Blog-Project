package gdh012.blog.domain.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class CategoryDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Request {
        @NotBlank(message = "카테고리 이름은 필수입니다.")
        private String name;

        public Request(String name) {
            this.name = name;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Connect {
        @NotNull
        private List<Long> boardIds;

        public Connect(List<Long> boardIds) {
            this.boardIds = boardIds;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Update {
        @NotBlank(message = "카테고리 이름은 필수입니다.")
        private String name;

        public Update(String name) {
            this.name = name;
        }
    }

    @Getter
    @Builder
    public static class Response {
        private final Long categoryId;
        private final String name;

        public Response(Long categoryId, String name) {
            this.categoryId = categoryId;
            this.name = name;
        }
    }
}
