package ar.edu.undec.core.order.model;

import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.model.UserStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private Long id;
    private User user;
    private OrderStatus status;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor privado
    private Order(Long id,
                  User user,
                  OrderStatus status,
                  BigDecimal amount,
                  LocalDateTime createdAt,
                  LocalDateTime updatedAt) {

        this.id = id;
        this.user = user;
        this.status = status;
        this.amount = amount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // =========================
    // FACTORY METHOD
    // =========================
    public static Order createNew(User user,
                                  BigDecimal amount,
                                  LocalDateTime now) {

        if (user == null)
            throw new IllegalArgumentException("El usuario no puede ser nulo");

        if (user.getStatus() != UserStatus.ACTIVE)
            throw new IllegalStateException("Solo usuarios ACTIVOS pueden crear órdenes");

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto debe ser mayor a cero");

        if (now == null)
            throw new IllegalArgumentException("La fecha actual no puede ser nula");

        return new Order(
                null,
                user,
                OrderStatus.PENDING,
                amount,
                now,
                now
        );
    }

    // =========================
    // TRANSICIONES DE ESTADO
    // =========================
    public void changeStatus(OrderStatus newStatus, LocalDateTime now) {

        if (newStatus == null)
            throw new IllegalArgumentException("El nuevo estado no puede ser nulo");

        if (!isValidTransition(this.status, newStatus))
            throw new IllegalStateException("Transición de estado inválida: "
                    + this.status + " -> " + newStatus);

        this.status = newStatus;
        this.updatedAt = now;
    }

    private boolean isValidTransition(OrderStatus from, OrderStatus to) {
        switch (from) {
            case PENDING:
                return to == OrderStatus.PROCESSING
                        || to == OrderStatus.CANCELLED;

            case PROCESSING:
                return to == OrderStatus.APPROVED
                        || to == OrderStatus.REJECTED;

            default:
                return false;
        }
    }

    // =========================
    // GETTERS
    // =========================
    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // =========================
    // MÉTODO PARA SETEAR ID
    // (usado por infraestructura)
    // =========================
    public void assignId(Long id) {
        if (this.id != null)
            throw new IllegalStateException("La orden ya tiene ID asignado");
        this.id = id;
    }

    // =========================
    // equals & hashCode
    // =========================
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
