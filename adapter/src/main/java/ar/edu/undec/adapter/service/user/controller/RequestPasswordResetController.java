package ar.edu.undec.adapter.service.user.controller;

import ar.edu.undec.adapter.data.user.crud.UserCRUD;
import ar.edu.undec.core.user.input.RequestPasswordResetUserInput;
import ar.edu.undec.core.exceptions.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class RequestPasswordResetController {

    private final UserCRUD userCRUD;
    private final RequestPasswordResetUserInput input;

    public RequestPasswordResetController(UserCRUD userCRUD,
                                          RequestPasswordResetUserInput input) {
        this.userCRUD = userCRUD;
        this.input = input;
    }

    @PatchMapping("{userId}/password")
    public ResponseEntity<Void> requestReset(@PathVariable Long userId) {
        String email = userCRUD.findById(userId)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"))
                .getEmail();

        input.execute(email);
        return ResponseEntity.ok().build();
    }
}
