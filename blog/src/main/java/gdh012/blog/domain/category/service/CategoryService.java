package gdh012.blog.domain.category.service;


import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.domain.board.entity.Board;
import gdh012.blog.domain.board.service.BoardService;
import gdh012.blog.domain.category.dto.CategoryDto;
import gdh012.blog.domain.category.entity.Category;
import gdh012.blog.domain.category.repository.CategoryRepository;
import gdh012.blog.global.exception.code.BusinessLogicException;
import gdh012.blog.global.exception.code.ExceptionCode;
import gdh012.blog.global.utils.AuthUserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BoardService boardService;
    private final AuthUserUtil authUserUtil;

    public CategoryDto.Response createCategory(CategoryDto.Request requestDto) {
        Account findAccount = authUserUtil.getAuthUser();

        if (findAccount.getCategories().stream()
                .anyMatch(category -> category.getName().equals(requestDto.getName())))
            throw new BusinessLogicException(ExceptionCode.CATEGORY_ALREADY_EXISTS);

        Category category = Category.builder()
                .name(requestDto.getName())
                .account(findAccount)
                .build();

        category = categoryRepository.save(category);
        findAccount.addCategory(category);

        return CategoryDto.Response.builder()
                .categoryId(category.getCategoryId())
                .build();
    }

    public void connectCategory(Long categoryId, CategoryDto.Connect connectDto) {
        Account findAccount = authUserUtil.getAuthUser();

        Category findCategory = getMyCategory(categoryId, findAccount);

        connectDto.getBoardIds()
                .forEach(boardId -> {
                    Board board = boardService.getMyBoard(boardId, findAccount);
                    board.updateCategory(findCategory);
                    findCategory.addBoard(board);
                });
    }

    public CategoryDto.Response getCategory(Long categoryId) {
        Account findAccount = authUserUtil.getAuthUser();

        Category findCategory = getMyCategory(categoryId, findAccount);

        return CategoryDto.Response.builder()
                .categoryId(findCategory.getCategoryId())
                .name(findCategory.getName())
                .build();
    }

    public List<CategoryDto.Response> getCategories() {
        Account findAccount = authUserUtil.getAuthUser();

        return findAccount.getCategories().stream()
                .map(category -> CategoryDto.Response.builder()
                        .categoryId(category.getCategoryId())
                        .name(category.getName())
                        .build())
                .toList();
    }

    public void updateCategory(Long categoryId, CategoryDto.Update updateDto) {
        Account findAccount = authUserUtil.getAuthUser();

        Category findCategory = getMyCategory(categoryId, findAccount);

        if (categoryRepository.findAll().stream()
                        .anyMatch(category -> category.getName().equals(updateDto.getName())))
            throw new BusinessLogicException(ExceptionCode.CATEGORY_ALREADY_EXISTS);

        findCategory.toBuilder()
                .name(updateDto.getName())
                .build();
    }

    public void deleteCategory(Long categoryId) {
        Account findAccount = authUserUtil.getAuthUser();

        Category findCategory = getMyCategory(categoryId, findAccount);

        findCategory.getBoards()
                .forEach(board -> board.updateCategory(null));
        findAccount.getCategories().remove(findCategory);
        categoryRepository.delete(findCategory);
    }

    private Category getMyCategory(Long categoryId, Account findAccount) {
        Category findCategory = categoryRepository.findById(categoryId).orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND));

        if (findCategory.getAccount().getAccountId() != findAccount.getAccountId())
            throw new BusinessLogicException(ExceptionCode.CATEGORY_NOT_ALLOW);
        return findCategory;
    }
}
