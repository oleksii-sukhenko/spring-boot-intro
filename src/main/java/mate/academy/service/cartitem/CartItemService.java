package mate.academy.service.cartitem;

import mate.academy.dto.cartitem.CartItemDto;
import mate.academy.dto.cartitem.CartItemRequestDto;
import mate.academy.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.model.ShoppingCart;

public interface CartItemService {
    CartItemDto save(CartItemRequestDto requestDto, ShoppingCart shoppingCart);

    CartItemDto update(CartItemUpdateRequestDto requestDto, Long id);

    void delete(Long id);
}
