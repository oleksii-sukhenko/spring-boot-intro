package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.dto.shoppingcart.cartitem.CartItemRequestDto;
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
import mate.academy.service.shoppingcart.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartItemMapper cartItemMapper;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @Test
    @DisplayName("Verify getAllInfo() returns ShoppingCartDto for given userId")
    void getAllInfo_ValidUserId_ReturnsShoppingCartDto() {
        Long userId = 1L;
        ShoppingCart shoppingCart = new ShoppingCart()
                .setId(1L)
                .setUser(new User()
                        .setId(userId)
                        .setEmail("user1@mail.com")
                        .setPassword("password")
                        .setFirstName("First")
                        .setLastName("Last")
                );
        ShoppingCartDto expected = new ShoppingCartDto();
        expected.setId(1L);

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);

        ShoppingCartDto result = shoppingCartService.getAllInfo(userId);

        assertNotNull(result);
        assertEquals(expected, result);

        verify(shoppingCartRepository).findByUserId(userId);
        verify(shoppingCartMapper).toDto(shoppingCart);
        verifyNoMoreInteractions(shoppingCartRepository, shoppingCartMapper);
    }

    @Test
    @DisplayName("Verify addBookToCart() creates new cart when user has none")
    void addBookToCart_UserHasNotCart_CreatesNewCart() {
        Long userId = 1L;
        Long bookId = 1L;
        int quantity = 2;
        CartItemRequestDto requestDto = new CartItemRequestDto()
                .setBookId(bookId)
                .setQuantity(quantity);

        User user = new User().setId(userId);

        Book book = new Book().setId(bookId);

        ShoppingCart shoppingCart = new ShoppingCart()
                .setUser(user)
                .setCartItems(new HashSet<>());

        CartItem cartItem = new CartItem()
                .setBook(book)
                .setShoppingCart(shoppingCart)
                .setQuantity(quantity);

        ShoppingCartDto expect = new ShoppingCartDto();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(null);
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        when(cartItemMapper.toModel(requestDto)).thenReturn(cartItem);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);
        when(shoppingCartMapper.toDto(any(ShoppingCart.class))).thenReturn(expect);

        ShoppingCartDto result = shoppingCartService.addBookToCart(requestDto, userId);

        assertNotNull(result);
        assertEquals(expect, result);
        verify(userRepository).findById(userId);
        verify(bookRepository).findById(bookId);
        verify(shoppingCartRepository).findByUserId(userId);
        verify(cartItemRepository).save(any(CartItem.class));
        verify(shoppingCartMapper).toDto(any(ShoppingCart.class));
    }

    @Test
    @DisplayName("Verify deleteCartItem() calls repository method deleteById()")
    void deleteCartItem_ValidId_CallsDeleteById() {
        Long cartItemId = 1L;

        shoppingCartService.deleteCartItem(cartItemId);

        verify(cartItemRepository).deleteById(cartItemId);
        verifyNoMoreInteractions(cartItemRepository);
    }
}
