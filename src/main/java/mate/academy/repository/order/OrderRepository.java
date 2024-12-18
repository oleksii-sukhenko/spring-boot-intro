package mate.academy.repository.order;

import java.util.List;
import java.util.Optional;
import mate.academy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndUserId(
            @Param("userId") Long id,
            @Param("orderId") Long userId
    );

    List<Order> findAllByUserId(@Param("userId") Long userId);
}
