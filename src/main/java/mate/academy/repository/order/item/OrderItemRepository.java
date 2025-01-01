package mate.academy.repository.order.item;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrder(Order order);

    List<OrderItem> findByOrderId(
            @Param("orderId") Long orderId
    );

    @Query(value = """
        SELECT oi FROM OrderItem oi
        JOIN FETCH oi.order o
        WHERE oi.id = :orderItemId AND o.id = :orderId AND o.user.id = :userId""")
    Optional<OrderItem> findByIdAndOrderIdAndUserId(@Param("orderItemId") Long orderItemId,
                                                    @Param("orderId") Long orderId,
                                                    @Param("userId") Long userId);
}
