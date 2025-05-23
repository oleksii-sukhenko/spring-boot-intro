package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.shoppingcart.ShoppingCartDto;
import mate.academy.dto.shoppingcart.cartitem.CartItemRequestDto;
import mate.academy.dto.shoppingcart.cartitem.CartItemUpdateRequestDto;
import mate.academy.model.User;
import mate.academy.service.shoppingcart.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing shopping carts")
@RestController
@RequestMapping("/cart")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    @Operation(
            summary = "Get information about shopping cart",
            description = "Get all available information about shopping cart")
    public ShoppingCartDto getAllInfoAboutShoppingCart(
            @AuthenticationPrincipal User user
    ) {
        return shoppingCartService.getAllInfo(user.getId());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping
    @Operation(
            summary = "Add book to shopping cart",
            description = "Add number of book to shopping cart"
    )
    public ShoppingCartDto addBookToShoppingCart(
            @RequestBody @Valid CartItemRequestDto requestDto,
            @AuthenticationPrincipal User user
    ) {
        return shoppingCartService.addBookToCart(requestDto, user.getId());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping("/cartItem/{cartItemId}")
    @Operation(
            summary = "Change number of books",
            description = "Change number of the books in shopping cart"
    )
    public ShoppingCartDto changeNumberOfBooks(
            @RequestBody @Valid CartItemUpdateRequestDto requestDto,
            @PathVariable("cartItemId") Long id,
            @AuthenticationPrincipal User user
    ) {
        return shoppingCartService.updateBooksQuantity(requestDto, id, user.getId());
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @DeleteMapping("/cartItem/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete book from shopping cart",
            description = "Delete the book from shopping cart"
    )
    public void deleteBookFromShoppingCart(
            @PathVariable("cartItemId") Long id
    ) {
        shoppingCartService.deleteCartItem(id);
    }
}
