package mate.academy.service.order;

import jakarta.transaction.Transactional;
import java.util.List;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.dto.order.OrderUpdateRequestDto;
import mate.academy.dto.order.item.OrderItemResponseDto;

public interface OrderService {

    @Transactional
    OrderResponseDto addOrder(Long userId, OrderRequestDto createOrderRequestDto);

    List<OrderResponseDto> getAllOrders(Long userId);

    OrderResponseDto updateOrderStatus(Long id, OrderRequestDto requestDto);

    @Transactional
    OrderResponseDto updateOrderStatus(Long id, OrderUpdateRequestDto requestDto);

    List<OrderItemResponseDto> getAllItemsFromOrder(Long orderId);

    OrderItemResponseDto getItemFromOrderById(Long orderId, Long itemId);
}
