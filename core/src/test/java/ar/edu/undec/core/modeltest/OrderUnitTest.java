package ar.edu.undec.core.modeltest;

import ar.edu.undec.core.order.model.OrderStatus;
import ar.edu.undec.core.user.model.User;

import ar.edu.undec.core.exceptions.InvalidStateException;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import ar.edu.undec.core.order.model.Order;

public class OrderUnitTest {

    private User createActiveUser() {

        LocalDateTime now = LocalDateTime.now();

        User user = User.createNew(
                "correo@padilla.com",
                "pass123",
                "codigo123",
                now.plusDays(1),
                now
        );

        user.activate("codigo123", now);

        return user;
    }

    private User createPendingUser() {

        LocalDateTime now = LocalDateTime.now();

        return User.createNew(
                "correo@padilla.com",
                "pass123",
                "codigo123",
                now.plusDays(1),
                now
        );
    }

    // =========================
    // TESTS
    // =========================

    @Test
    void createOrder_ShouldInitializePendingStatus_WhenUserIsActive() {

        User user = createActiveUser();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal amount = BigDecimal.valueOf(100);

        Order order = Order.createNew(user, amount, now);

        assertEquals(user, order.getUser());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertEquals(amount, order.getAmount());
        assertEquals(now, order.getCreatedAt());
        assertEquals(now, order.getUpdatedAt());
    }

    @Test
    void createOrder_ShouldThrowException_WhenUserIsNotActive() {

        User user = createPendingUser();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal amount = BigDecimal.valueOf(100);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> Order.createNew(user, amount, now)
        );

        assertEquals("Solo usuarios ACTIVOS pueden crear órdenes",
                exception.getMessage());
    }

    @Test
    void changeStatus_ShouldAllowValidTransitions() {

        User user = createActiveUser();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal amount = BigDecimal.valueOf(100);

        Order order = Order.createNew(user, amount, now);

        // PENDING → PROCESSING
        order.changeStatus(OrderStatus.PROCESSING, now);
        assertEquals(OrderStatus.PROCESSING, order.getStatus());

        // PROCESSING → APPROVED
        order.changeStatus(OrderStatus.APPROVED, now);
        assertEquals(OrderStatus.APPROVED, order.getStatus());
    }

    @Test
    void changeStatus_ShouldThrowException_WhenInvalidTransition() {

        User user = createActiveUser();
        LocalDateTime now = LocalDateTime.now();
        BigDecimal amount = BigDecimal.valueOf(100);

        Order order = Order.createNew(user, amount, now);

        // Intento inválido: PENDING → APPROVED
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> order.changeStatus(OrderStatus.APPROVED, now)
        );

        assertTrue(exception.getMessage()
                .contains("Transición de estado inválida"));
    }
}
