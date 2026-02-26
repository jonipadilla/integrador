package ar.edu.undec.core.usecasetest;


import ar.edu.undec.core.exceptions.ValidationException;
import ar.edu.undec.core.order.model.Order;
import ar.edu.undec.core.order.model.OrderStatus;
import ar.edu.undec.core.order.repository.ChangeOrderStatusRepository;
import ar.edu.undec.core.order.usecase.ChangeOrderStatusUseCase;
import ar.edu.undec.core.time.TimeProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChangeOrderStatusUseCaseTest {

    @Mock
    private ChangeOrderStatusRepository repository;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private ChangeOrderStatusUseCase useCase;

    @Test
    void execute_ShouldThrowValidationException_WhenOrderIdIsNull() {
        ValidationException exception = assertThrows(ValidationException.class,
                () -> useCase.execute(null, OrderStatus.PROCESSING));

        assertEquals("El id de la orden no puede ser nulo", exception.getMessage());
    }

    @Test
    void execute_ShouldThrowValidationException_WhenOrderNotFound() {
        Long orderId = 1L;
        when(repository.findById(orderId)).thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class,
                () -> useCase.execute(orderId, OrderStatus.PROCESSING));

        assertEquals("Orden no encontrada", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void execute_ShouldChangeStatus_WhenTransitionIsValid_PendingToProcessing() {
        Long orderId = 2L;
        LocalDateTime now = LocalDateTime.of(2026, 2, 22, 10, 0);
        Order order = mock(Order.class);

        when(repository.findById(orderId)).thenReturn(Optional.of(order));
        when(timeProvider.now()).thenReturn(now);

        useCase.execute(orderId, OrderStatus.PROCESSING);

        verify(order).changeStatus(OrderStatus.PROCESSING, now);
        verify(repository).save(order);
    }

    @Test
    void execute_ShouldChangeStatus_WhenTransitionIsValid_ProcessingToApproved() {
        Long orderId = 3L;
        LocalDateTime now = LocalDateTime.of(2026, 2, 22, 11, 0);
        Order order = mock(Order.class);

        when(repository.findById(orderId)).thenReturn(Optional.of(order));
        when(timeProvider.now()).thenReturn(now);

        useCase.execute(orderId, OrderStatus.APPROVED);

        verify(order).changeStatus(OrderStatus.APPROVED, now);
        verify(repository).save(order);
    }

    @Test
    void execute_ShouldChangeStatus_WhenTransitionIsValid_PendingToCancelled() {
        Long orderId = 4L;
        LocalDateTime now = LocalDateTime.of(2026, 2, 22, 12, 0);
        Order order = mock(Order.class);

        when(repository.findById(orderId)).thenReturn(Optional.of(order));
        when(timeProvider.now()).thenReturn(now);

        useCase.execute(orderId, OrderStatus.CANCELLED);

        verify(order).changeStatus(OrderStatus.CANCELLED, now);
        verify(repository).save(order);
    }

    @Test
    void execute_ShouldThrowException_WhenTransitionIsInvalid() {
        Long orderId = 5L;
        LocalDateTime now = LocalDateTime.of(2026, 2, 22, 13, 0);
        Order order = mock(Order.class);

        when(repository.findById(orderId)).thenReturn(Optional.of(order));
        when(timeProvider.now()).thenReturn(now);
        doThrow(new IllegalStateException("Transición inválida"))
                .when(order).changeStatus(OrderStatus.APPROVED, now);

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> useCase.execute(orderId, OrderStatus.APPROVED));

        assertEquals("Transición inválida", exception.getMessage());
        verify(repository, never()).save(any());
    }
}
