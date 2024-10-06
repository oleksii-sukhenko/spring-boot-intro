package mate.academy.service.shoppingcart;

import mate.academy.dto.cartitem.CartItemRequestDto;
import mate.academy.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getAllInfo(Long userId);

    ShoppingCartDto addBookToCart(CartItemRequestDto requestDto, Long userId);

    ShoppingCartDto updateBooksQuantity(CartItemUpdateRequestDto requestDto, Long id, Long userId);

    void deleteCartItem(Long id);
}