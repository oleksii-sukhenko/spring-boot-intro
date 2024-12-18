package mate.academy.service.order;

import java.util.List;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.dto.order.OrderUpdateRequestDto;
import mate.academy.dto.order.item.OrderItemResponseDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto addOrder(Long userId, OrderRequestDto createOrderRequestDto);

    List<OrderResponseDto> getAllOrders(Long userId, Pageable pageable);

    OrderResponseDto updateOrderStatus(Long id, OrderUpdateRequestDto requestDto);

    List<OrderItemResponseDto> getAllItemsFromOrder(Long orderId);

    OrderItemResponseDto getItemFromOrderById(Long orderId, Long itemId);
}
