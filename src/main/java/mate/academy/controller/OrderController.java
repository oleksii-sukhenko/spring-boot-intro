package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.dto.order.OrderUpdateRequestDto;
import mate.academy.dto.order.item.OrderItemResponseDto;
import mate.academy.model.User;
import mate.academy.service.order.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Place order", description = "Place order for authenticated user")
    public ResponseEntity<OrderResponseDto> placeOrder(
            @RequestBody @Valid OrderRequestDto request,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(orderService.createOrder(user.getId(), request));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Watch order history",
            description = "Watch order history for authenticated user"
    )
    public ResponseEntity<Page<OrderResponseDto>> getOrderHistory(
            @AuthenticationPrincipal User user,
            @PageableDefault(sort = "orderDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(orderService.getUserOrderHistory(user.getId(), pageable));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Update order status",
            description = "Update order using orderId (only for admin role)"
    )
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long id,
            @RequestBody @Valid OrderUpdateRequestDto request) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, request));
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get orderItems", description = "Get orderItems using orderId")
    public ResponseEntity<List<OrderItemResponseDto>> getOrderItems(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderItems(orderId));
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(
            summary = "Get orderItem",
            description = "Get orderItem using orderId and itemId"
    )
    public ResponseEntity<OrderItemResponseDto> getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(orderService.getOrderItem(orderId, itemId));
    }
}
