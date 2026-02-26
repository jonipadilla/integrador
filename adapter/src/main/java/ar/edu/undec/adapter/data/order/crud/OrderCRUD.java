package ar.edu.undec.adapter.data.order.crud;

import ar.edu.undec.adapter.data.order.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

public interface OrderCRUD extends CrudRepository<OrderEntity, Long>{


}
