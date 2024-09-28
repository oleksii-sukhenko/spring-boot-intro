package mate.academy.service.shoppingcart.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.cartitem.CartItemDto;
import mate.academy.dto.cartitem.CartItemRequestDto;
import mate.academy.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.service.cartitem.CartItemService;
import mate.academy.service.shoppingcart.ShoppingCartService;
import mate.academy.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemService cartItemService;

    @Override
    public ShoppingCartDto getAllInfo(Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        User user = userService.findById(authenticatedUser.getId());
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public CartItemDto addBookToCart(CartItemRequestDto requestDto, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        User user = userService.findById(authenticatedUser.getId());
        ShoppingCart userShoppingCart = shoppingCartRepository.findByUserId(user.getId());
        if (userShoppingCart == null) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUser(user);
            userShoppingCart = shoppingCartRepository.save(shoppingCart);
        }
        return cartItemService.save(requestDto, userShoppingCart);
    }

    @Override
    @Transactional
    public CartItemDto updateBooksQuantity(CartItemUpdateRequestDto requestDto, Long id) {
        return cartItemService.update(requestDto, id);
    }

    @Override
    @Transactional
    public void deleteCartItem(Long id, Authentication authentication) {
        User authenticatedUser = (User) authentication.getPrincipal();
        User user = userService.findById(authenticatedUser.getId());
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        shoppingCart.getCartItems().removeIf(cartItem -> cartItem.getId().equals(id));
        shoppingCartRepository.save(shoppingCart);
        cartItemService.delete(id);
    }
}
