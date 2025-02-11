package gdh012.blog.domain.comment.controller;


import gdh012.blog.domain.comment.dto.CommentDto;
import gdh012.blog.domain.comment.service.CommentService;
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
@RequestMapping("/comments")
@RestController
public class CommentController {
    private static final String COMMENT_DEFAULT_URL = "/comments";

    private final CommentService commentService;

    @PostMapping("/post/{board-id}")
    public ResponseEntity<HttpStatus> postComment(@Positive @PathVariable("board-id") Long boardId,
                                                  @Valid @RequestBody CommentDto.Request requestDto) {
        CommentDto.Response responseDto = commentService.createComment(boardId, requestDto);
        URI location = UriCreator.createUri(COMMENT_DEFAULT_URL, responseDto.getCommentId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{comment-id}")
    public ResponseEntity<SingleResponse<CommentDto.Response>> getComment(@Positive @PathVariable("comment-id") Long commentId) {
        CommentDto.Response responseDto = commentService.getComment(commentId);

        return ResponseEntity.ok(SingleResponse.success(responseDto));
    }

    @PatchMapping("/update/{comment-id}")
    public ResponseEntity<HttpStatus> updateComment(@Positive @PathVariable("comment-id") Long commentId,
                                                  @Valid @RequestBody CommentDto.Update updateDto) {
        commentService.updateComment(commentId, updateDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity<HttpStatus> deleteBoard(@Positive @PathVariable("comment-id") Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.noContent().build();
    }
}
