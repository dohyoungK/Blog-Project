package gdh012.blog.domain.board.service;

import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.domain.board.dto.BoardDto;
import gdh012.blog.domain.board.entity.Board;
import gdh012.blog.domain.board.repository.BoardRepository;
import gdh012.blog.global.exception.code.BusinessLogicException;
import gdh012.blog.global.exception.code.ExceptionCode;
import gdh012.blog.global.utils.AuthUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final AuthUserUtil authUserUtil;

    public BoardDto.Response createBoard(BoardDto.Post postDto) {
        Account findAccount = authUserUtil.getAuthUser();

        Board board = Board.builder()
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .account(findAccount)
                .build();

        board = boardRepository.save(board);
        findAccount.addBoard(board);

        return BoardDto.Response.builder()
                .boardId(board.getBoardId())
                .build();
    }

    public BoardDto.Response getBoard(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));

        return BoardDto.Response.builder()
                .boardId(findBoard.getBoardId())
                .title(findBoard.getTitle())
                .content(findBoard.getContent())
                .build();
    }

    public Page<BoardDto.Response> getBoards(int page, int size) {
        Account findAccount = authUserUtil.getAuthUser();

        List<BoardDto.Response> responses = findAccount.getBoards().stream()
                .map(board -> BoardDto.Response.builder()
                        .boardId(board.getBoardId())
                        .title(board.getTitle())
                        .content(board.getContent())
                        .build())
                .toList();

        int startIdx = page * size;
        int endIdx = Math.min(responses.size(), (page + 1) * size);
        return new PageImpl<>(responses.subList(startIdx, endIdx), PageRequest.of(page, size), responses.size());
    }

    public void updateBoard(Long boardId, BoardDto.Update updateDto) {
        Account findAccount = authUserUtil.getAuthUser();

        Board findBoard = getMyBoard(boardId, findAccount);
        findBoard.updateBoard(updateDto.getTitle(), updateDto.getContent());
    }

    public void deleteBoard(Long boardId) {
        Account findAccount = authUserUtil.getAuthUser();

        Board findBoard = getMyBoard(boardId, findAccount);
        findAccount.getBoards().remove(findBoard);
        boardRepository.delete(findBoard);
    }

    private Board getMyBoard(Long boardId, Account findAccount) {
        Board findBoard = boardRepository.findById(boardId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));

        // 찾은 게시물이 로그인된 사용자의 게시물인지 확인
        if (findBoard.getAccount().getAccountId() != findAccount.getAccountId())
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_ALLOW);

        return findBoard;
    }
}
