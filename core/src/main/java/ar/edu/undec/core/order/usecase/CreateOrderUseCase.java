package ar.edu.undec.core.order.usecase;

import ar.edu.undec.core.order.input.CreateOrderInput;
import ar.edu.undec.core.order.input.OrderDTO;
import ar.edu.undec.core.order.model.Order;
import ar.edu.undec.core.order.repository.CreateOrderRepository;
import ar.edu.undec.core.order.repository.FindUserRepository;
import ar.edu.undec.core.time.TimeProvider;
import ar.edu.undec.core.user.model.User;

import java.math.BigDecimal;

public class CreateOrderUseCase implements CreateOrderInput {


    private final CreateOrderRepository orderRepository;
    private final FindUserRepository findUserRepository;
    private final TimeProvider timeProvider;

    public CreateOrderUseCase(CreateOrderRepository orderRepository,
                              FindUserRepository findUserRepository,
                              TimeProvider timeProvider) {
        this.orderRepository = orderRepository;
        this.findUserRepository = findUserRepository;
        this.timeProvider = timeProvider;
    }

    @Override
    public OrderDTO execute(Long userId, BigDecimal amount) {

        User user = findUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Order order = Order.createNew(
                user,
                amount,
                timeProvider.now()
        );

        Order savedOrder = orderRepository.save(order);

        return new OrderDTO(
                savedOrder.getId(),
                savedOrder.getUser().getId(),
                savedOrder.getStatus().name(),
                savedOrder.getAmount(),
                savedOrder.getCreatedAt(),
                savedOrder.getUpdatedAt()
        );
    }
}
