package mate.academy.dto.shoppingcart.cartitem;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CartItemDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private int quantity;
}
