package gdh012.blog.domain.board.controller;

import gdh012.blog.domain.board.dto.BoardDto;
import gdh012.blog.domain.board.service.BoardService;
import gdh012.blog.global.response.PageResponse;
import gdh012.blog.global.response.SingleResponse;
import gdh012.blog.global.utils.UriCreator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Validated
@RequiredArgsConstructor
@RequestMapping("/boards")
@RestController
public class BoardController {
    private static final String BOARD_DEFAULT_URL = "/boards";

    private final BoardService boardService;

    @PostMapping("/post")
    public ResponseEntity<HttpStatus> postBoard(@Valid @RequestBody BoardDto.Post postDto) {
        BoardDto.Response responseDto = boardService.createBoard(postDto);
        URI location = UriCreator.createUri(BOARD_DEFAULT_URL, responseDto.getBoardId());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{board-id}") // 이건 단일 조회라 게시물 아이디 받아서
    public ResponseEntity<SingleResponse<BoardDto.Response>> getBoard(@Positive @PathVariable("board-id") Long boardId) {
        BoardDto.Response responseDto = boardService.getBoard(boardId);

        return ResponseEntity.ok(SingleResponse.success(responseDto));
    }

    @GetMapping
    public ResponseEntity<PageResponse<BoardDto.Response>> getBoards(@Positive @RequestParam(name = "page", defaultValue = "1") int page,
                                                                     @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        Page<BoardDto.Response> responseDto = boardService.getBoards(page - 1, size);

        return ResponseEntity.ok(PageResponse.success(responseDto.getContent(), responseDto));
    }

    @PatchMapping("/update/{board-id}")
    public ResponseEntity<HttpStatus> updateBoard(@Positive @PathVariable("board-id") Long boardId,
                                                  @Valid @RequestBody BoardDto.Update updateDto) {
        boardService.updateBoard(boardId, updateDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{board-id}")
    public ResponseEntity<HttpStatus> deleteBoard(@Positive @PathVariable("board-id") Long boardId) {
        boardService.deleteBoard(boardId);

        return ResponseEntity.noContent().build();
    }
}
