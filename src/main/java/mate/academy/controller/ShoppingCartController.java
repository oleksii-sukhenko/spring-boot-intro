package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.cartitem.CartItemDto;
import mate.academy.dto.cartitem.CartItemRequestDto;
import mate.academy.dto.cartitem.CartItemUpdateRequestDto;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.service.shoppingcart.ShoppingCartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
@RestController
@RequestMapping(value = "/cart")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    @Operation(
            summary = "Get information about shopping cart",
            description = "Get all available information about shopping cart")
    public ShoppingCartDto getAllInfoAboutShoppingCart(Authentication authentication) {
        return shoppingCartService.getAllInfo(authentication);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @Operation(
            summary = "Add book to shopping cart",
            description = "Add number of book to shopping cart"
    )
    public CartItemDto addBookToShoppingCart(
            @RequestBody CartItemRequestDto requestDto,
            Authentication authentication
    ) {
        return shoppingCartService.addBookToCart(requestDto, authentication);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/cartItem/{cartItemId}")
    @Operation(
            summary = "Change number of books",
            description = "Change number of the books in shopping cart"
    )
    public CartItemDto changeNumberOfBooks(
            @RequestBody @Valid CartItemUpdateRequestDto requestDto,
            @PathVariable("cartItemId") Long id
    ) {
        return shoppingCartService.updateBooksQuantity(requestDto, id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @DeleteMapping("/cartItem/{cartItemId}")
    @Operation(
            summary = "Delete book from shopping cart",
            description = "Delete the book from shopping cart"
    )
    public void deleteBookFromShoppingCart(
            @PathVariable("cartItemId") Long id,
            Authentication authentication
    ) {
        shoppingCartService.deleteCartItem(id, authentication);
    }
}
