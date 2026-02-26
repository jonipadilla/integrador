package ar.edu.undec.core.user.usecase;

import ar.edu.undec.core.exceptions.ValidationException;
import ar.edu.undec.core.time.TimeProvider;
import ar.edu.undec.core.user.input.ResetPasswordUserInput;
import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.repository.ResetPasswordUserRepository;

public class ResetPasswordUserUseCase implements ResetPasswordUserInput {

    private final ResetPasswordUserRepository repository;
    private final TimeProvider timeProvider;

    public ResetPasswordUserUseCase(ResetPasswordUserRepository repository,
                                    TimeProvider timeProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
    }

    @Override
    public void execute(String email,
                        String resetCode,
                        String newPassword) {

        if (email == null || email.isBlank())
            throw new ValidationException("El correo electrónico no puede estar en blanco");

        if (resetCode == null || resetCode.isBlank())
            throw new ValidationException("El código de reset no puede estar en blanco");

        if (newPassword == null || newPassword.isBlank())
            throw new ValidationException("La nueva contraseña no puede estar en blanco");

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"));

        user.resetPassword(resetCode, newPassword, timeProvider.now());

        repository.save(user);
    }
}
