package mate.academy.service.shoppingcart;

import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.dto.shoppingcart.cartitem.CartItemRequestDto;
import mate.academy.dto.shoppingcart.cartitem.CartItemUpdateRequestDto;
import mate.academy.model.User;

public interface ShoppingCartService {
    ShoppingCartDto getAllInfo(Long userId);

    ShoppingCartDto addBookToCart(CartItemRequestDto requestDto, Long userId);

    ShoppingCartDto updateBooksQuantity(CartItemUpdateRequestDto requestDto, Long id, Long userId);

    void deleteCartItem(Long id);

    void createShoppingCart(User user);
}
