package ar.edu.undec.core.usecasetest;

import ar.edu.undec.core.exceptions.ValidationException;
import ar.edu.undec.core.time.TimeProvider;
import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.model.UserStatus;
import ar.edu.undec.core.user.repository.ResetPasswordUserRepository;
import ar.edu.undec.core.user.usecase.ResetPasswordUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResetPasswordUserUseCaseTest {

    @Mock
    private ResetPasswordUserRepository repository;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private ResetPasswordUserUseCase resetPasswordUserUseCase;



    //Reset correcto

    @Test
    void execute_ShouldResetPassword_WhenDataIsValid() {

        LocalDateTime now = LocalDateTime.now();

        User user = User.createNew(
                "correo@padilla.com",
                "pass123",
                "codigo123",
                now.plusDays(1),
                now
        );

        // Activamos usuario
        user.activate("codigo123", now);

        // Solicita reset
        user.requestPasswordReset("reset123", now.plusDays(1));

        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.of(user));

        when(timeProvider.now()).thenReturn(now);

        resetPasswordUserUseCase.execute(
                "correo@padilla.com",
                "reset123",
                "newPassword123"
        );

        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals("newPassword123", user.getPassword());

        verify(repository, times(1)).save(user);
    }


    //Usuario no encontrado

    @Test
    void execute_ShouldThrowException_WhenUserNotFound() {

        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.empty());

        assertThrows(ValidationException.class,
                () -> resetPasswordUserUseCase.execute(
                        "correo@padilla.com",
                        "reset123",
                        "newPassword"
                ));

        verify(repository, never()).save(any());
    }


    //Código inválido

    @Test
    void execute_ShouldThrowException_WhenResetCodeIsInvalid() {

        LocalDateTime now = LocalDateTime.now();

        User user = User.createNew(
                "correo@padilla.com",
                "pass123",
                "codigo123",
                now.plusDays(1),
                now
        );

        user.activate("codigo123", now);
        user.requestPasswordReset("reset123", now.plusDays(1));

        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.of(user));

        when(timeProvider.now()).thenReturn(now);

        assertThrows(ValidationException.class,
                () -> resetPasswordUserUseCase.execute(
                        "correo@padilla.com",
                        "codigoIncorrecto",
                        "newPassword"
                ));

        verify(repository, never()).save(any());
    }


    //Código vencido

    @Test
    void execute_ShouldThrowException_WhenResetCodeIsExpired() {

        LocalDateTime now = LocalDateTime.now();

        User user = User.createNew(
                "correo@padilla.com",
                "pass123",
                "codigo123",
                now.plusDays(1),
                now
        );

        user.activate("codigo123", now);

        // Reset vencido
        user.requestPasswordReset("reset123", now.minusDays(1));

        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.of(user));

        when(timeProvider.now()).thenReturn(now);

        assertThrows(IllegalStateException.class,
                () -> resetPasswordUserUseCase.execute(
                        "correo@padilla.com",
                        "reset123",
                        "newPassword"
                ));

        verify(repository, never()).save(any());
    }


    //Usuario no está en RESET

    @Test
    void execute_ShouldThrowException_WhenUserIsNotInResetState() {

        LocalDateTime now = LocalDateTime.now();

        User user = User.createNew(
                "correo@padilla.com",
                "pass123",
                "codigo123",
                now.plusDays(1),
                now
        );

        user.activate("codigo123", now);
        // NO pedimos reset

        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.of(user));

        when(timeProvider.now()).thenReturn(now);

        assertThrows(IllegalStateException.class,
                () -> resetPasswordUserUseCase.execute(
                        "correo@padilla.com",
                        "reset123",
                        "newPassword"
                ));

        verify(repository, never()).save(any());
    }
}
