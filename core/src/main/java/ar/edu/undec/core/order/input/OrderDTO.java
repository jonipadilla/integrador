package ar.edu.undec.core.order.input;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDTO {

    private Long id;
    private Long userId;
    private String status;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OrderDTO(Long id,
                    Long userId,
                    String status,
                    BigDecimal amount,
                    LocalDateTime createdAt,
                    LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.amount = amount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getStatus() { return status; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
