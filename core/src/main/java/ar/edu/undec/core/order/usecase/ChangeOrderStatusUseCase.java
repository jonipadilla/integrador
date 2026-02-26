package ar.edu.undec.core.order.usecase;

import ar.edu.undec.core.exceptions.ValidationException;
import ar.edu.undec.core.order.input.ChangeOrderStatusInput;
import ar.edu.undec.core.order.model.Order;
import ar.edu.undec.core.order.model.OrderStatus;
import ar.edu.undec.core.order.repository.ChangeOrderStatusRepository;
import ar.edu.undec.core.time.TimeProvider;

public class ChangeOrderStatusUseCase implements ChangeOrderStatusInput {

    private final ChangeOrderStatusRepository repository;
    private final TimeProvider timeProvider;

    public ChangeOrderStatusUseCase(ChangeOrderStatusRepository repository,
                                    TimeProvider timeProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
    }

    public void execute(Long orderId, OrderStatus newStatus) {

        if (orderId == null)
            throw new ValidationException("El id de la orden no puede ser nulo");

        Order order = repository.findById(orderId)
                .orElseThrow(() -> new ValidationException("Orden no encontrada"));

        order.changeStatus(newStatus, timeProvider.now());

        repository.save(order);
    }
}
