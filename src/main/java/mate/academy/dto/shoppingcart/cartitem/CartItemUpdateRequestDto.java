package mate.academy.dto.shoppingcart.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CartItemUpdateRequestDto {
    @Positive
    private int quantity;
}
