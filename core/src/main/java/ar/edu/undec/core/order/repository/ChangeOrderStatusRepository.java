package ar.edu.undec.core.order.repository;

import ar.edu.undec.core.order.model.Order;

import java.util.Optional;

public interface ChangeOrderStatusRepository {

    Optional<Order> findById(Long id);

    void save(Order order);
}
