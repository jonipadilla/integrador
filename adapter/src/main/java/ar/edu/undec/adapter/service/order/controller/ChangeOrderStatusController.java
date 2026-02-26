package ar.edu.undec.adapter.service.order.controller;

import ar.edu.undec.core.order.input.ChangeOrderStatusInput;
import ar.edu.undec.core.order.model.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("orders")


public class ChangeOrderStatusController {


    private final ChangeOrderStatusInput input;

    public ChangeOrderStatusController(ChangeOrderStatusInput input) {
        this.input = input;
    }

    public record ChangeStatusRequest(
            @NotBlank String newStatus
    ) {}

    @PatchMapping("{orderId}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable Long orderId,
                                             @Valid @RequestBody ChangeStatusRequest request) {
        OrderStatus status = OrderStatus.valueOf(request.newStatus().toUpperCase());
        input.execute(orderId, status);
        return ResponseEntity.ok().build();
    }
}
