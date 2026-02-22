package ar.edu.undec.core.usecasetest;

import ar.edu.undec.core.order.input.OrderDTO;
import ar.edu.undec.core.order.model.Order;
import ar.edu.undec.core.order.repository.CreateOrderRepository;
import ar.edu.undec.core.order.repository.FindUserRepository;
import ar.edu.undec.core.order.usecase.CreateOrderUseCase;
import ar.edu.undec.core.time.TimeProvider;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.model.UserStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class CreateOrderUseCaseTest {


    @Mock
    private CreateOrderRepository orderRepository;

    @Mock
    private FindUserRepository findUserRepository;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private CreateOrderUseCase useCase;

    @Test
    void shouldCreateOrderSuccessfully() {

        // Arrange
        Long userId = 1L;
        BigDecimal amount = new BigDecimal("200.00");
        LocalDateTime now = LocalDateTime.now();

        User user = org.mockito.Mockito.mock(User.class);
        when(user.getStatus()).thenReturn(UserStatus.ACTIVE);
        when(user.getId()).thenReturn(userId);

        when(findUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(timeProvider.now())
                .thenReturn(now);

        Order savedOrder = Order.createNew(user, amount, now);
        savedOrder.assignId(10L);

        when(orderRepository.save(any(Order.class)))
                .thenReturn(savedOrder);

        // Act
        OrderDTO result = useCase.execute(userId, amount);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(userId, result.getUserId());
        assertEquals("PENDING", result.getStatus());
        assertEquals(amount, result.getAmount());
        assertEquals(now, result.getCreatedAt());
        assertEquals(now, result.getUpdatedAt());
    }

    @Test
    void shouldThrowExceptionIfUserNotFound() {

        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100.00");

        when(findUserRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(userId, amount));
    }

    @Test
    void shouldThrowExceptionIfUserIsNotActive() {

        Long userId = 1L;
        BigDecimal amount = new BigDecimal("100.00");

        User user = org.mockito.Mockito.mock(User.class);
        when(user.getStatus()).thenReturn(UserStatus.PENDING);

        when(findUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        assertThrows(IllegalStateException.class,
                () -> useCase.execute(userId, amount));
    }

    @Test
    void shouldThrowExceptionIfAmountIsInvalid() {

        Long userId = 1L;
        BigDecimal amount = BigDecimal.ZERO;

        User user = org.mockito.Mockito.mock(User.class);
        when(user.getStatus()).thenReturn(UserStatus.ACTIVE);

        when(findUserRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(timeProvider.now())
                .thenReturn(LocalDateTime.now());

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(userId, amount));
    }
}
