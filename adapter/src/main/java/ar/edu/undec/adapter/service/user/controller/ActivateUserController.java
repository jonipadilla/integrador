package ar.edu.undec.adapter.service.user.controller;


import ar.edu.undec.core.user.input.ActivateUserInput;
import ar.edu.undec.core.user.input.UserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("users")

public class ActivateUserController {

    private final ActivateUserInput input;

    public ActivateUserController(ActivateUserInput input) {
        this.input = input;
    }

    public record ActivateUserRequest(
            @Email @NotBlank String email,
            @NotBlank String activationCode
    ) {}

    @PutMapping("activate")
    public ResponseEntity<UserDTO> activate(@Valid @RequestBody ActivateUserRequest request) {
        UserDTO dto = input.execute(request.email(), request.activationCode());
        return ResponseEntity.ok(dto);
    }
}
