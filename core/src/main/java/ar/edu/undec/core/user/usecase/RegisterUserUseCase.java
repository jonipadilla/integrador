package ar.edu.undec.core.user.usecase;

import ar.edu.undec.core.exceptions.EmailAlreadyExistsException;
import ar.edu.undec.core.exceptions.ValidationException;
import ar.edu.undec.core.time.TimeProvider;
import ar.edu.undec.core.user.input.RegisterUserInput;
import ar.edu.undec.core.user.input.UserDTO;
import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.repository.RegisterUserRepository;

import java.time.LocalDateTime;
import java.util.UUID;


public class RegisterUserUseCase implements RegisterUserInput {

    private final RegisterUserRepository userRepository;
    private final TimeProvider timeProvider;

    public RegisterUserUseCase(RegisterUserRepository userRepository,
                               TimeProvider timeProvider) {
        this.userRepository = userRepository;
        this.timeProvider = timeProvider;
    }

    @Override
    public UserDTO execute(String email, String password) {

        if (email == null || email.isBlank())
            throw new ValidationException("El correo electrónico no puede estar en blanco");

        if (password == null || password.isBlank())
            throw new ValidationException("La contraseña no puede estar en blanco");

        if (userRepository.existsByEmail(email))
            throw new EmailAlreadyExistsException("El correo electrónico ya está registrado");

        LocalDateTime now = timeProvider.now();

        String activationCode = UUID.randomUUID().toString();

        User user = User.createNew(
                email,
                password,
                activationCode,
                now.plusDays(1),
                now
        );

        userRepository.save(user);

        return new UserDTO(
                user.getEmail(),
                user.getStatus(),
                user.getCreatedAt()
        );
    }
}
