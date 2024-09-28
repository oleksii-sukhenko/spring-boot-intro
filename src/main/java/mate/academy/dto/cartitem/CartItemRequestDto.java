package mate.academy.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CartItemRequestDto {
    @NotNull
    private Long bookId;
    @Positive
    private int quantity;
}
