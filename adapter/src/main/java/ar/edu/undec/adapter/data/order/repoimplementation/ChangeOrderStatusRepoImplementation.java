package ar.edu.undec.adapter.data.order.repoimplementation;

import ar.edu.undec.adapter.data.order.crud.OrderCRUD;
import ar.edu.undec.adapter.data.order.mapper.OrderMapper;
import ar.edu.undec.core.order.model.Order;
import ar.edu.undec.core.order.repository.ChangeOrderStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ChangeOrderStatusRepoImplementation implements ChangeOrderStatusRepository{

    private final OrderCRUD orderCRUD;

    public ChangeOrderStatusRepoImplementation(OrderCRUD orderCRUD) {
        this.orderCRUD = orderCRUD;
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderCRUD.findById(id).map(OrderMapper::mapEntityToCore);
    }

    @Override
    public void save(Order order) {
        orderCRUD.save(OrderMapper.mapCoreToEntity(order));
    }
}
