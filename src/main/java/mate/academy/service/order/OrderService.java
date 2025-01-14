package mate.academy.service.order;

import java.util.List;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.dto.order.OrderUpdateRequestDto;
import mate.academy.dto.order.item.OrderItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(Long userId, OrderRequestDto request);

    Page<OrderResponseDto> getUserOrderHistory(Long userId, Pageable pageable);

    OrderResponseDto updateOrderStatus(Long id, OrderUpdateRequestDto request);

    List<OrderItemResponseDto> getOrderItems(Long orderId);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId);
}
