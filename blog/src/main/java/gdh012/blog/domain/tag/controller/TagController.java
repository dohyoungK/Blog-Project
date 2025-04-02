package gdh012.blog.domain.tag.controller;

import gdh012.blog.domain.comment.dto.CommentDto;
import gdh012.blog.domain.comment.service.CommentService;
import gdh012.blog.domain.tag.dto.TagDto;
import gdh012.blog.domain.tag.service.TagService;
import gdh012.blog.global.response.SingleResponse;
import gdh012.blog.global.utils.UriCreator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Validated
@RequiredArgsConstructor
@RequestMapping("/tags")
@RestController
public class TagController {
    private static final String TAG_DEFAULT_URL = "/tags";

    private final TagService tagService;

    @PostMapping("/post/{board-id}")
    public ResponseEntity<HttpStatus> postTag(@Positive @PathVariable("board-id") Long boardId,
                                                  @Valid @RequestBody TagDto.Request requestDto) {
        TagDto.Response responseDto = tagService.createTag(boardId, requestDto);
        URI location = UriCreator.createUri(TAG_DEFAULT_URL, responseDto.getTagId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{tag-id}")
    public ResponseEntity<SingleResponse<TagDto.Response>> getComment(@Positive @PathVariable("tag-id") Long tagId) {
        TagDto.Response responseDto = tagService.getTag(tagId);

        return ResponseEntity.ok(SingleResponse.success(responseDto));
    }

    @PatchMapping("/update/{tag-id}")
    public ResponseEntity<HttpStatus> updateTag(@Positive @PathVariable("tag-id") Long tagId,
                                                    @Valid @RequestBody TagDto.Request requestDto) {
        tagService.updateTag(tagId, requestDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{tag-id}")
    public ResponseEntity<HttpStatus> deleteBoard(@Positive @PathVariable("tag-id") Long tagId) {
        tagService.deleteTag(tagId);

        return ResponseEntity.noContent().build();
    }
}
