package ar.edu.undec.core.order.input;

import java.math.BigDecimal;

public interface CreateOrderInput {

    OrderDTO execute(Long userId, BigDecimal amount);
}
