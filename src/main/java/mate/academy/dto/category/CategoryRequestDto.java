package mate.academy.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CategoryRequestDto {
    @NotBlank
    @Size(min = 2, max = 16)
    private String name;
    @Size(max = 64)
    private String description;
}
