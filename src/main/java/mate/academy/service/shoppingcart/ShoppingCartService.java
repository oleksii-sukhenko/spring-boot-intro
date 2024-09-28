package mate.academy.service.shoppingcart;

import mate.academy.dto.cartitem.CartItemDto;
import mate.academy.dto.cartitem.CartItemRequestDto;
import mate.academy.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto getAllInfo(Authentication authentication);

    CartItemDto addBookToCart(CartItemRequestDto requestDto, Authentication authentication);

    CartItemDto updateBooksQuantity(CartItemUpdateRequestDto requestDto, Long id);

    void deleteCartItem(Long id, Authentication authentication);
}
