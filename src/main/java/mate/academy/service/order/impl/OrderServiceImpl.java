package mate.academy.service.order.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.dto.order.OrderUpdateRequestDto;
import mate.academy.dto.order.item.OrderItemResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.exception.OrderProcessingException;
import mate.academy.mapper.OrderItemMapper;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.CartItem;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.order.OrderRepository;
import mate.academy.repository.order.item.OrderItemRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.repository.user.UserRepository;
import mate.academy.service.order.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserRepository userRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public OrderResponseDto createOrder(Long userId, OrderRequestDto request) {
        User user = findUserById(userId);
        ShoppingCart cart = getShoppingCartWithValidation(userId);

        Order order = buildOrder(user, request, cart);
        saveOrderAndClearCart(order, cart);

        return orderMapper.toDto(order);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()
                        -> new EntityNotFoundException("User not found with id " + userId));
    }

    private ShoppingCart getShoppingCartWithValidation(Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId);
        if (cart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Shopping cart is empty for user with id " + userId);
        }
        return cart;
    }

    private Order buildOrder(User user, OrderRequestDto request, ShoppingCart cart) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(Order.Status.PENDING);

        Set<OrderItem> orderItems = buildOrderItems(order, cart);
        order.setOrderItems(orderItems);
        order.setTotal(calculateTotal(orderItems));

        return order;
    }

    private Set<OrderItem> buildOrderItems(Order order, ShoppingCart cart) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void saveOrderAndClearCart(Order order, ShoppingCart cart) {
        orderRepository.save(order);
        cart.getCartItems().clear();
        shoppingCartRepository.save(cart);
    }

    @Override
    public Page<OrderResponseDto> getUserOrderHistory(Long userId, Pageable pageable) {
        Page<Order> orders = orderRepository.findAllByUserId(userId, pageable);
        return orders.map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long id, OrderUpdateRequestDto request) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id " + id));
        order.setStatus(Order.Status.valueOf(request.getStatus()));
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItems(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found with id " + orderId));
        List<OrderItem> orderItemsList = new ArrayList<>(order.getOrderItems());
        return orderItemMapper.toDto(orderItemsList);
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByOrderIdAndId(orderId, itemId)
                .orElseThrow(()
                        -> new EntityNotFoundException("OrderItem not found with id " + itemId));
        return orderItemMapper.toDto(orderItem);
    }
}
