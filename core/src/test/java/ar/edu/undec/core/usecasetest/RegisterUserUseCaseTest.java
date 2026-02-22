package ar.edu.undec.core.usecasetest;

import ar.edu.undec.core.exceptions.EmailAlreadyExistsException;
import ar.edu.undec.core.exceptions.ValidationException;
import ar.edu.undec.core.time.TimeProvider;
import ar.edu.undec.core.user.input.UserDTO;
import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.model.UserStatus;
import ar.edu.undec.core.user.repository.RegisterUserRepository;
import ar.edu.undec.core.user.usecase.RegisterUserUseCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;


@ExtendWith(MockitoExtension.class)

public class RegisterUserUseCaseTest {

    @Mock
    private RegisterUserRepository userRepository;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @Test
    void execute_ShouldReturnUserDTO_WhenEmailAndPasswordAreValid() {

        LocalDateTime fixedNow = LocalDateTime.now();

        when(userRepository.existsByEmail("correo@padilla.com"))
                .thenReturn(false);

        when(timeProvider.now())
                .thenReturn(fixedNow);

        UserDTO userDTO = registerUserUseCase.execute("correo@padilla.com", "pass123");

        assertEquals("correo@padilla.com", userDTO.getEmail());
        assertEquals(UserStatus.PENDING, userDTO.getStatus());
        assertEquals(fixedNow, userDTO.getCreatedAt());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void execute_ShouldThrowEmailAlreadyExistsException_WhenEmailIsDuplicate() {

        when(userRepository.existsByEmail("correo@padilla.com"))
                .thenReturn(true);

        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> registerUserUseCase.execute("correo@padilla.com", "pass123")
        );

        assertEquals("El correo electrónico ya está registrado", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void execute_ShouldThrowValidationException_WhenEmailIsBlank() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> registerUserUseCase.execute("", "pass123")
        );

        assertEquals("El correo electrónico no puede estar en blanco", exception.getMessage());
    }

    @Test
    void execute_ShouldThrowValidationException_WhenPasswordIsBlank() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> registerUserUseCase.execute("correo@padilla.com", "")
        );

        assertEquals("La contraseña no puede estar en blanco", exception.getMessage());
    }
}
