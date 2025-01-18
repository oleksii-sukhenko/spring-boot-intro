package mate.academy.mapper;

import java.util.List;
import java.util.Set;
import mate.academy.config.MapperConfig;
import mate.academy.dto.order.item.OrderItemResponseDto;
import mate.academy.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface OrderItemMapper {
    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toDto(OrderItem orderItem);

    List<OrderItemResponseDto> toDto(List<OrderItem> orderItems);

    Set<OrderItemResponseDto> toDto(Set<OrderItem> orderItems);
}
