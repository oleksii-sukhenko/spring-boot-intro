package mate.academy.repository.order.item;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findAllByOrder(Order order);

    List<OrderItem> findByOrderId(
            @Param("orderId") Long orderId
    );

    Optional<OrderItem> findByIdAndOrderId(
            @Param("itemId") Long id,
            @Param("orderId") Long orderId
    );
}
