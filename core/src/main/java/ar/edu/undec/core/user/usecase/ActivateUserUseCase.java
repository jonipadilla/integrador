package ar.edu.undec.core.user.usecase;

import ar.edu.undec.core.exceptions.InvalidStateException;
import ar.edu.undec.core.exceptions.ValidationException;
import ar.edu.undec.core.time.TimeProvider;
import ar.edu.undec.core.user.input.ActivateUserInput;
import ar.edu.undec.core.user.input.UserDTO;
import ar.edu.undec.core.user.model.User;

import ar.edu.undec.core.user.repository.ActivateUserRepository;

public class ActivateUserUseCase implements ActivateUserInput {


    private final ActivateUserRepository repository;
    private final TimeProvider timeProvider;

    public ActivateUserUseCase(ActivateUserRepository repository,
                               TimeProvider timeProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
    }

    public UserDTO execute(String email, String activationCode) {

        if (email == null || email.isBlank())
            throw new ValidationException("El correo electrónico no puede estar en blanco");

        if (activationCode == null || activationCode.isBlank())
            throw new ValidationException("El código de activación no puede estar en blanco");

        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("Usuario no encontrado"));

        // 👉 El dominio valida estado, código y expiración
        user.activate(activationCode, timeProvider.now());

        repository.save(user);

        return new UserDTO(
                user.getEmail(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
