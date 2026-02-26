package ar.edu.undec.adapter.data.order.repoimplementation;


import ar.edu.undec.adapter.data.order.crud.OrderCRUD;
import ar.edu.undec.adapter.data.order.entity.OrderEntity;
import ar.edu.undec.adapter.data.order.mapper.OrderMapper;
import ar.edu.undec.core.order.model.Order;
import ar.edu.undec.core.order.repository.CreateOrderRepository;
import org.springframework.stereotype.Repository;

@Repository

public class CreateOrderRepoImplementation implements CreateOrderRepository{

    private final OrderCRUD orderCRUD;

    public CreateOrderRepoImplementation(OrderCRUD orderCRUD) {
        this.orderCRUD = orderCRUD;
    }

    @Override
    public Order save(Order order) {
        OrderEntity saved = orderCRUD.save(OrderMapper.mapCoreToEntity(order));

        if (order.getId() == null && saved.getId() != null) {
            order.assignId(saved.getId());
        }
        return order;
    }
}
