package ar.edu.undec.adapter.service.user.controller;


import ar.edu.undec.adapter.data.user.crud.UserCRUD;
import ar.edu.undec.core.exceptions.ValidationException;
import ar.edu.undec.core.user.input.ResetPasswordUserInput;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")

public class ResetPasswordController {

    private final UserCRUD userCRUD;
    private final ResetPasswordUserInput input;

    public ResetPasswordController(UserCRUD userCRUD,
                                   ResetPasswordUserInput input) {
        this.userCRUD = userCRUD;
        this.input = input;
    }

    public record ResetPasswordRequest(
            @NotBlank String resetCode,
            @NotBlank String newPassword
    ) {}

    @PutMapping("{userId}/password")
    public ResponseEntity<Void> reset(@PathVariable Long userId,
                                      @Valid @RequestBody ResetPasswordRequest request) {

        String email = userCRUD.findById(userId)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"))
                .getEmail();

        input.execute(email, request.resetCode(), request.newPassword());
        return ResponseEntity.ok().build();
    }
}
