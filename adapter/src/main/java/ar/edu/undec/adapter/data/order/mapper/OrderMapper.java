package ar.edu.undec.adapter.data.order.mapper;

import ar.edu.undec.adapter.data.order.entity.OrderEntity;
import ar.edu.undec.adapter.data.user.mapper.UserMapper;
import ar.edu.undec.core.order.model.Order;


public class OrderMapper {

    public static OrderEntity mapCoreToEntity(Order order) {
        return new OrderEntity(
                order.getId(),
                UserMapper.mapCoreToEntity(order.getUser()),
                order.getStatus(),
                order.getAmount(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    public static Order mapEntityToCore(OrderEntity entity) {
        return Order.factoryFromEntity(
                entity.getId(),
                UserMapper.mapEntityToCore(entity.getUser()),
                entity.getStatus(),
                entity.getAmount(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
