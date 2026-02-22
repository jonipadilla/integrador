package ar.edu.undec.core.order.repository;

import ar.edu.undec.core.order.model.Order;

public interface CreateOrderRepository {


    Order save(Order order);
}
