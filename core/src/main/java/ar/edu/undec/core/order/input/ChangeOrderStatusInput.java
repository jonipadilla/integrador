package ar.edu.undec.core.order.input;

import ar.edu.undec.core.order.model.OrderStatus;

public interface ChangeOrderStatusInput {

    void execute(Long orderId, OrderStatus newStatus);
}
