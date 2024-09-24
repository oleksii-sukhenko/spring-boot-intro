package mate.academy.mapper;

import java.util.List;
import mate.academy.config.MapperConfig;
import mate.academy.dto.category.CategoryDto;
import mate.academy.dto.category.CategoryRequestDto;
import mate.academy.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    List<CategoryDto> toDtos(List<Category> categories);

    Category toModel(CategoryRequestDto requestDto);

    void updateCategoryFromDto(CategoryRequestDto requestDto, @MappingTarget Category category);
}
