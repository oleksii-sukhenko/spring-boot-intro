package mate.academy.mapper;

import java.util.List;
import mate.academy.config.MapperConfig;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toDto(Order order);

    List<OrderResponseDto> toDto(List<Order> orders); // Мапінг для списків

    Order toModel(OrderRequestDto createOrderRequestDto);
}
