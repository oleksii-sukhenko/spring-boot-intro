package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
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
import mate.academy.service.shoppingcart.impl.ShoppingCartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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

    private ShoppingCart shoppingCart;
    private CartItem cartItem;
    private CartItemUpdateRequestDto requestDto;
    private ShoppingCartDto shoppingCartDto;
    private User user;
    private Book book;
    private Long userId;
    private Long cartItemId;
    private Long bookId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        bookId = 1L;
        cartItemId = 1L;
        cartItem = new CartItem()
                .setId(cartItemId)
                .setShoppingCart(shoppingCart)
                .setQuantity(1);

        requestDto = new CartItemUpdateRequestDto().setQuantity(5);

        shoppingCartDto = new ShoppingCartDto();

        user = new User()
                .setId(userId)
                .setEmail("user1@mail.com")
                .setPassword("password")
                .setFirstName("First")
                .setLastName("Last");

        book = new Book().setId(bookId);

        shoppingCart = new ShoppingCart()
                .setId(1L)
                .setUser(user)
                .setCartItems(new HashSet<>());
    }

    @Test
    @DisplayName("Verify getAllInfo() returns ShoppingCartDto for given userId")
    void getAllInfo_ValidUserId_ReturnsShoppingCartDto() {
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
        int quantity = 2;
        CartItemRequestDto requestDto = new CartItemRequestDto()
                .setBookId(bookId)
                .setQuantity(quantity);

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
    @DisplayName(
            "Verify updateBooksQuantity() returns ShoppingCartDto with updated quantity"
    )
    void updateBooksQuantity_ValidId_UpdateCart() {
        when(shoppingCartRepository.findByUserId(userId)).thenReturn(shoppingCart);
        when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId()))
                .thenReturn(Optional.of(cartItem));

        cartItem.setShoppingCart(shoppingCart);

        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto result
                = shoppingCartService.updateBooksQuantity(requestDto, cartItemId, userId);

        assertNotNull(result);
        assertEquals(shoppingCartDto, result);
        assertEquals(5, cartItem.getQuantity());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    @DisplayName("Verify deleteCartItem() calls repository method deleteById()")
    void deleteCartItem_ValidId_CallsDeleteById() {
        shoppingCartService.deleteCartItem(cartItemId);

        verify(cartItemRepository).deleteById(cartItemId);
        verifyNoMoreInteractions(cartItemRepository);
    }

    @Test
    void updateBooksQuantity_NotExistingCartItemId_ItemNotFound() {
        Long cartItemId = 2L;

        when(shoppingCartRepository.findByUserId(userId)).thenReturn(shoppingCart);
        when(cartItemRepository.findByIdAndShoppingCartId(cartItemId, shoppingCart.getId()))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                shoppingCartService.updateBooksQuantity(requestDto, cartItemId, userId));
    }

    @Test
    @DisplayName("Verify createShoppingCart() calls repository method save()")
    void createShoppingCart_ValidUser_CallsSave() {
        shoppingCartService.createShoppingCart(user);
        verify(shoppingCartRepository).save(any(ShoppingCart.class));
    }
}
