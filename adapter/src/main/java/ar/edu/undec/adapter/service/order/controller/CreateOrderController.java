package ar.edu.undec.adapter.service.order.controller;

import ar.edu.undec.core.order.input.CreateOrderInput;
import ar.edu.undec.core.order.input.OrderDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("users")


public class CreateOrderController {


    private final CreateOrderInput input;

    public CreateOrderController(CreateOrderInput input) {
        this.input = input;
    }

    public record CreateOrderRequest(
            @NotNull @Positive BigDecimal amount
    ) {}

    @PostMapping("{userId}/orders")
    public ResponseEntity<OrderDTO> create(@PathVariable Long userId,
                                           @Valid @RequestBody CreateOrderRequest request) {
        OrderDTO dto = input.execute(userId, request.amount());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }
}
