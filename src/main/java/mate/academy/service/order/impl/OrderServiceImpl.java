package mate.academy.service.order.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.dto.order.OrderUpdateRequestDto;
import mate.academy.dto.order.item.OrderItemResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.exception.OrderProcessingException;
import mate.academy.mapper.OrderItemMapper;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.order.OrderRepository;
import mate.academy.repository.order.item.OrderItemRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.repository.user.UserRepository;
import mate.academy.service.order.OrderService;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;

    @Override
    public OrderResponseDto addOrder(Long userId, OrderRequestDto createOrderRequestDto) {
        ShoppingCart shoppingCart = getValidatedShoppingCart(userId);
        User user = getUserById(userId);

        BigDecimal total = calculateTotalPrice(shoppingCart);

        Order order = buildOrder(createOrderRequestDto, user, total);

        Set<OrderItem> orderItems = mapCartItemsToOrderItems(shoppingCart, order);
        order.setOrderItems(orderItems);

        Order savedOrder = saveOrderAndClearCart(order, shoppingCart);

        return orderMapper.toDto(savedOrder);
    }

    private ShoppingCart getValidatedShoppingCart(Long userId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId);
        if (shoppingCart == null || shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Shopping cart is empty for user ID: " + userId);
        }
        return shoppingCart;
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("User not found with ID: " + userId));
    }

    private BigDecimal calculateTotalPrice(ShoppingCart shoppingCart) {
        return shoppingCart.getCartItems().stream()
                .map(
                        item -> item.getBook().getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order buildOrder(OrderRequestDto createOrderRequestDto, User user, BigDecimal total) {
        Order order = orderMapper.toModel(createOrderRequestDto);
        order.setUser(user);
        order.setTotal(total);
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(createOrderRequestDto.getAddress());
        return order;
    }

    private Set<OrderItem> mapCartItemsToOrderItems(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    return orderItem;
                })
                .collect(Collectors.toSet());
    }

    private Order saveOrderAndClearCart(Order order, ShoppingCart shoppingCart) {
        Order savedOrder = orderRepository.save(order);
        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);
        return savedOrder;
    }

    @Override
    public List<OrderResponseDto> getAllOrders(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<Order> orders = orderRepository.findAllByUserId(userId, pageable);
        orders.forEach(order -> {
            Hibernate.initialize(order.getOrderItems());
            order.getOrderItems().forEach(orderItem ->
                    Hibernate.initialize(orderItem.getBook()));
        });
        return orders.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long id, OrderUpdateRequestDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Order not found"));
        order.setStatus(Order.Status.valueOf(requestDto.status()));
        orderRepository.save(order);

        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderItemResponseDto> getAllItemsFromOrder(Long orderId) {
        return orderItemRepository.findById(orderId).stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemResponseDto getItemFromOrderById(Long orderId, Long itemId) {
        User user = userRepository.findById(orderId).orElseThrow(()
                -> new EntityNotFoundException("User not found"));
        OrderItem orderItem
                = orderItemRepository.findByIdAndOrderIdAndUserId(itemId, orderId, user.getId())
                .orElseThrow(() -> new RuntimeException("Order item not found"));
        return orderItemMapper.toDto(orderItem);
    }
}
