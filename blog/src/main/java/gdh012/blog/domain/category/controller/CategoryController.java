package gdh012.blog.domain.category.controller;

import gdh012.blog.domain.category.dto.CategoryDto;
import gdh012.blog.domain.category.service.CategoryService;
import gdh012.blog.global.response.MultiResponse;
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
import java.util.List;

@Validated
@RequiredArgsConstructor
@RequestMapping("/categories")
@RestController

public class CategoryController {
    private static final String CATEGORY_DEFAULT_URL = "/categories";

    private final CategoryService categoryService;

    @PostMapping("/post")
    public ResponseEntity<HttpStatus> postCategory(@Valid @RequestBody CategoryDto.Request requestDto) {
        CategoryDto.Response responseDto = categoryService.createCategory(requestDto);
        URI location = UriCreator.createUri(CATEGORY_DEFAULT_URL, responseDto.getCategoryId());

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/connect/{category-id}")
    public ResponseEntity<HttpStatus> connectCategory(@Positive @PathVariable("category-id") Long categoryId,
                                                      @Valid @RequestBody CategoryDto.Connect connectDto) {
        categoryService.connectCategory(categoryId, connectDto);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{category-id}")
    public ResponseEntity<SingleResponse<CategoryDto.Response>> getCategory(@Positive @PathVariable("category-id") Long categoryId) {
        CategoryDto.Response responseDto = categoryService.getCategory(categoryId);

        return ResponseEntity.ok(SingleResponse.success(responseDto));
    }

    @GetMapping
    public ResponseEntity<MultiResponse<CategoryDto.Response>> getCategories() {
        List<CategoryDto.Response> responseDtos = categoryService.getCategories();

        return ResponseEntity.ok(MultiResponse.success(responseDtos));
    }

    @PatchMapping("/{category-id}")
    public ResponseEntity<HttpStatus> updateCategory(@Positive @PathVariable("category-id") Long categoryId,
                                                     @Valid @RequestBody CategoryDto.Update updateDto) {
        categoryService.updateCategory(categoryId, updateDto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{category-id}")
    public ResponseEntity<HttpStatus> deleteCategory(@Positive @PathVariable("category-id") Long categoryId) {
        categoryService.deleteCategory(categoryId);

        return ResponseEntity.noContent().build();
    }
}
