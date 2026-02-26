package ar.edu.undec.adapter.service.user.controller;

import ar.edu.undec.core.user.input.RegisterUserInput;
import ar.edu.undec.core.user.input.UserDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("users")
public class RegisterUserController {

    private final RegisterUserInput input;

    @Autowired
    public RegisterUserController(RegisterUserInput input) {
        this.input = input;
    }

    // Request simple (estilo cátedra) sin crear paquete dto
    public record RegisterUserRequest(
            @Email @NotBlank String email,
            @NotBlank String password
    ) {}

    @PostMapping
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody RegisterUserRequest request) {
        UserDTO userDTO = input.execute(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(userDTO);
    }
}
