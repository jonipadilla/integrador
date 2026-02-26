package ar.edu.undec.core.user.usecase;

import ar.edu.undec.core.exceptions.ValidationException;
import ar.edu.undec.core.time.TimeProvider;
import ar.edu.undec.core.user.input.RequestPasswordResetUserInput;
import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.model.UserStatus;
import ar.edu.undec.core.user.repository.ResetPasswordUserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequestPasswordResetUserUseCase implements RequestPasswordResetUserInput {


    private final ResetPasswordUserRepository repository;
    private final TimeProvider timeProvider;

    public RequestPasswordResetUserUseCase(ResetPasswordUserRepository repository,
                                           TimeProvider timeProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
    }

    @Override
    public void execute(String email) {

        // Validación de email
        if (email == null || email.isBlank()) {
            throw new ValidationException("El correo electrónico no puede estar en blanco");
        }

        // Buscar usuario por email
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"));

        // Solo usuarios ACTIVE pueden solicitar reset
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("Solo usuarios ACTIVE pueden solicitar reset de contraseña");
        }

        // Generar código de reset único
        String resetCode = UUID.randomUUID().toString();

        // Fecha de expiración 1 día desde ahora
        LocalDateTime expiration = timeProvider.now().plusDays(1);

        // Cambiar estado a RESET y asignar código + expiración
        user.requestPasswordReset(resetCode, expiration);

        // Guardar cambios
        repository.save(user);
    }

}
