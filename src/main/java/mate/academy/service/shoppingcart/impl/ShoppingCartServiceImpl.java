package mate.academy.service.shoppingcart.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.cartitem.CartItemRequestDto;
import mate.academy.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.cartitem.CartItemRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
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

    private ShoppingCart createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return shoppingCartRepository.save(shoppingCart);
    }

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
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find user with id " + userId)
        );
        ShoppingCart shoppingCart = createShoppingCart(user);
        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(requestDto.getQuantity());
        shoppingCart.getCartItems().add(cartItem);
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
}
