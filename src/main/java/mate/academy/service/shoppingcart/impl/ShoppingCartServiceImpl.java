package mate.academy.service.shoppingcart.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.dto.shoppingcart.cartitem.CartItemRequestDto;
import mate.academy.dto.shoppingcart.cartitem.CartItemUpdateRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CartItemMapper;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.repository.shoppingcart.cartitem.CartItemRepository;
import mate.academy.repository.user.UserRepository;
import mate.academy.service.shoppingcart.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getAllInfo(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto addBookToCart(CartItemRequestDto requestDto, Long userId) {
        Long bookId = requestDto.getBookId();
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Can't find book with id " + bookId)
        );
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);

        CartItem cartItem = getCartItemByBook(shoppingCart, requestDto);
        if (cartItem.getShoppingCart() == null) {
            cartItem.setBook(book);
            cartItem.setShoppingCart(shoppingCart);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
        }
        cartItemRepository.save(cartItem);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateBooksQuantity(
            CartItemUpdateRequestDto requestDto,
            Long id,
            Long userId
    ) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, cart.getId())
                        .map(item -> {
                            item.setQuantity(requestDto.getQuantity());
                            return item;
                        }).orElseThrow(
                                () -> new EntityNotFoundException("Can't find item with id: " + id)
                );
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }

    @Override
    public void deleteCartItem(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    private CartItem getCartItemByBook(
            ShoppingCart shoppingCart, CartItemRequestDto cartItemRequestDto
    ) {
        return shoppingCart.getCartItems().stream()
                .filter(
                        item -> item.setShoppingCart(shoppingCart)
                                .getId().equals(shoppingCart.getId()
                        )
                && item.getBook().getId().equals(cartItemRequestDto.getBookId()))
                .findFirst()
                .orElseGet(() -> cartItemMapper.toModel(cartItemRequestDto));
    }
}
