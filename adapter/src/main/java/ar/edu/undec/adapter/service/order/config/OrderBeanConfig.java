package ar.edu.undec.adapter.service.order.config;

import ar.edu.undec.core.order.input.ChangeOrderStatusInput;
import ar.edu.undec.core.order.input.CreateOrderInput;
import ar.edu.undec.core.order.repository.ChangeOrderStatusRepository;
import ar.edu.undec.core.order.repository.CreateOrderRepository;
import ar.edu.undec.core.order.repository.FindUserRepository;
import ar.edu.undec.core.order.usecase.ChangeOrderStatusUseCase;
import ar.edu.undec.core.order.usecase.CreateOrderUseCase;
import ar.edu.undec.core.time.TimeProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderBeanConfig {

    @Bean
    public CreateOrderInput createOrderInput(CreateOrderRepository orderRepository,
                                             FindUserRepository findUserRepository,
                                             TimeProvider timeProvider) {
        return new CreateOrderUseCase(orderRepository, findUserRepository, timeProvider);
    }

    @Bean
    public ChangeOrderStatusInput changeOrderStatusInput(ChangeOrderStatusRepository repository,
                                                         TimeProvider timeProvider) {
        return new ChangeOrderStatusUseCase(repository, timeProvider);
    }
}
