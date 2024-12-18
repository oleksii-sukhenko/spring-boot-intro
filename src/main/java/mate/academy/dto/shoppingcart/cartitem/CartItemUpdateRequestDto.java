package mate.academy.dto.shoppingcart.cartitem;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemUpdateRequestDto {
    @Positive
    private int quantity;
}
