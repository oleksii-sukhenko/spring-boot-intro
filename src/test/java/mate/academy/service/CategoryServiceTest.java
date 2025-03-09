package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CategoryRequestDto;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Category;
import mate.academy.repository.category.CategoryRepository;
import mate.academy.service.category.impl.CategoryServiceImpl;
import mate.academy.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify findAll() returns list of CategoryDto")
    void findAll_GivenPageable_ReturnsListOfCategoryDto() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> expectedCategories = TestUtil.getCategories();
        List<CategoryDto> expectedCategoryDtos = expectedCategories.stream()
                .map(category -> new CategoryDto()
                        .setId(category.getId())
                        .setName(category.getName())
                        .setDescription(category.getDescription()))
                .toList();

        when(categoryRepository.findAll(pageable)).thenReturn(new PageImpl<>(expectedCategories));
        when(categoryMapper.toDtos(expectedCategories)).thenReturn(expectedCategoryDtos);

        List<CategoryDto> result = categoryService.findAll(pageable);

        assertNotNull(result);
        assertEquals(expectedCategoryDtos, result);
        assertEquals(expectedCategories.size(), result.size());

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDtos(expectedCategories);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Should return CategoryDto when category exists")
    void getById_GivenValidId_ShouldReturnCategoryDto() {
        Long categoryId = 1L;
        Category expectedCategory = TestUtil.getCategory();
        CategoryDto expectedCategoryDto = TestUtil.getCategoryDto();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(expectedCategory));
        when(categoryMapper.toDto(expectedCategory)).thenReturn(expectedCategoryDto);

        CategoryDto result = categoryService.getById(categoryId);

        assertNotNull(result);
        assertEquals(expectedCategoryDto, result);

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toDto(expectedCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify save() method works")
    void save_ValidCategoryRequestDto_ReturnsCategoryDto() {
        when(categoryMapper.toModel(any(CategoryRequestDto.class)))
                .thenReturn(TestUtil.getCategory());
        when(categoryRepository.save(any(Category.class)))
                .thenReturn(TestUtil.getCategory());
        when(categoryMapper.toDto(any(Category.class)))
                .thenReturn(TestUtil.getCategoryDto());

        CategoryDto result = categoryService.save(TestUtil.categoryRequestDto());

        assertNotNull(result);
        assertEquals(TestUtil.getCategoryDto().getId(), result.getId());
        verify(categoryRepository).save(any(Category.class));
        verify(categoryMapper).toModel(any(CategoryRequestDto.class));
        verify(categoryMapper).toDto(any(Category.class));
    }

    @Test
    @DisplayName("Verify update() updates when category exists")
    void update_GivenValidId_ShouldUpdateCategoryAndReturnDto() {
        Long categoryId = 1L;
        Category existingCategory = TestUtil.getCategory();
        CategoryRequestDto updateRequest = TestUtil.categoryRequestDto();
        Category updatedCategory = TestUtil.getUpdatedCategory();
        CategoryDto expectedCategoryDto = TestUtil.getUpdatedCategoryDto();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        doNothing().when(categoryMapper).updateCategoryFromDto(updateRequest, existingCategory);
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedCategoryDto);

        CategoryDto result = categoryService.update(categoryId, updateRequest);

        assertNotNull(result);
        assertEquals(expectedCategoryDto, result);

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateCategoryFromDto(updateRequest, existingCategory);
        verify(categoryRepository).save(existingCategory);
        verify(categoryMapper).toDto(updatedCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }
}
