package gdh012.blog.domain.tag.service;

import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.domain.board.entity.Board;
import gdh012.blog.domain.board.service.BoardService;
import gdh012.blog.domain.tag.dto.TagDto;
import gdh012.blog.domain.tag.entity.Tag;
import gdh012.blog.domain.tag.repository.TagRepository;
import gdh012.blog.global.exception.code.BusinessLogicException;
import gdh012.blog.global.exception.code.ExceptionCode;
import gdh012.blog.global.utils.AuthUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class TagService {
    private final TagRepository tagRepository;
    private final BoardService boardService;
    private final AuthUserUtil authUserUtil;

    public TagDto.Response createTag(Long boardId, TagDto.Request requestDto) {
        Account findAccount = authUserUtil.getAuthUser();
        Board findBoard = boardService.getMyBoard(boardId, findAccount);

        Tag tag = Tag.builder()
                .name(requestDto.getName())
                .account(findAccount)
                .board(findBoard)
                .build();

        tag = tagRepository.save(tag);
        findAccount.addTag(tag);

        return TagDto.Response.builder()
                .tagId(tag.getTagId())
                .build();
    }

    public TagDto.Response getTag(Long tagId) {
        Account findAccount = authUserUtil.getAuthUser();
        Tag findTag = getMyTag(tagId, findAccount);

        return TagDto.Response.builder()
                .tagId(findTag.getTagId())
                .name(findTag.getName())
                .build();
    }

    public void updateTag(Long tagId, TagDto.Request requestDto) {
        Account findAccount = authUserUtil.getAuthUser();
        Tag findTag = getMyTag(tagId, findAccount);

        findTag.updateTag(requestDto.getName());
    }

    public void deleteTag(Long tagId) {
        Account findAccount = authUserUtil.getAuthUser();
        Tag findTag = getMyTag(tagId, findAccount);

        findAccount.getTags().remove(findTag);
        findTag.getBoard().getComments().remove(findTag);

        tagRepository.delete(findTag);
    }

    public Tag getMyTag(Long tagId, Account findAccount) {
        Tag findTag = tagRepository.findById(tagId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.TAG_NOT_FOUND));

        if (findTag.getAccount().getAccountId() != findAccount.getAccountId())
            throw new BusinessLogicException(ExceptionCode.TAG_NOT_ALLOW);

        return findTag;
    }
}
