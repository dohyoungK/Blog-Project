package gdh012.blog.domain.comment.service;

import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.domain.board.entity.Board;
import gdh012.blog.domain.board.service.BoardService;
import gdh012.blog.domain.comment.dto.CommentDto;
import gdh012.blog.domain.comment.entity.Comment;
import gdh012.blog.domain.comment.repository.CommentRepository;
import gdh012.blog.global.exception.code.BusinessLogicException;
import gdh012.blog.global.exception.code.ExceptionCode;
import gdh012.blog.global.utils.AuthUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardService boardService;
    private final AuthUserUtil authUserUtil;

    public CommentDto.Response createComment(Long boardId, CommentDto.Request requestDto) {
        Account findAccount = authUserUtil.getAuthUser();
        Board findBoard = boardService.getMyBoard(boardId, findAccount);

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .account(findAccount)
                .board(findBoard)
                .build();

        comment = commentRepository.save(comment);
        findAccount.addComment(comment);
        findBoard.addComment(comment);

        return CommentDto.Response.builder()
                .commentId(comment.getCommentId())
                .build();
    }

    public CommentDto.Response getComment(Long commentId) {
        Account findAccount = authUserUtil.getAuthUser();
        Comment findComment = getMyComment(commentId, findAccount);

        return CommentDto.Response.builder()
                .commentId(findComment.getCommentId())
                .content(findComment.getContent())
                .build();
    }

    public void updateComment(Long commentId, CommentDto.Update updateDto) {
        Account findAccount = authUserUtil.getAuthUser();
        Comment findComment = getMyComment(commentId, findAccount);

        findComment.updateComment(updateDto.getContent());
    }

    public void deleteComment(Long commentId) {
        Account findAccount = authUserUtil.getAuthUser();
        Comment findComment = getMyComment(commentId, findAccount);

        findAccount.getComments().remove(findComment);
        findComment.getBoard().getComments().remove(findComment);

        commentRepository.delete(findComment);
    }

    public Comment getMyComment(Long commentId, Account findAccount) {
        Comment findComment = commentRepository.findById(commentId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND));

        // 찾은 게시물이 로그인된 사용자의 게시물인지 확인
        if (findComment.getAccount().getAccountId() != findAccount.getAccountId())
            throw new BusinessLogicException(ExceptionCode.COMMENT_NOT_FOUND);

        return findComment;
    }
}
