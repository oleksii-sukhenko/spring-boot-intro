package mate.academy.repository.order.item;

import java.util.Optional;
import mate.academy.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrderIdAndId(Long orderId, Long itemId);
}
