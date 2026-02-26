package ar.edu.undec.core.usecasetest;

import ar.edu.undec.core.exceptions.ValidationException;
import ar.edu.undec.core.time.TimeProvider;
import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.model.UserStatus;
import ar.edu.undec.core.user.repository.ResetPasswordUserRepository;
import ar.edu.undec.core.user.usecase.RequestPasswordResetUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RequestPasswordResetUserUseCaseTest {

    @Mock
    private ResetPasswordUserRepository repository;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private RequestPasswordResetUserUseCase requestPasswordResetUserUseCase;

    // =========================
    // Caso feliz: usuario ACTIVE
    // =========================
    @Test
    void execute_ShouldRequestPasswordReset_WhenUserIsActive() {

        LocalDateTime now = LocalDateTime.of(2026, 2, 22, 10, 0);

        User user = User.createNew(
                "correo@padilla.com",
                "pass123",
                "activationCode123",
                now.plusDays(1),
                now
        );

        // Activamos usuario
        user.activate("activationCode123", now);

        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.of(user));

        when(timeProvider.now()).thenReturn(now);

        // Ejecutar UseCase
        requestPasswordResetUserUseCase.execute("correo@padilla.com");

        // Validaciones
        assertEquals(UserStatus.RESET, user.getStatus());
        assertNotNull(user.getResetCode());
        assertNotNull(user.getResetExpiresAt());
        assertTrue(user.getResetExpiresAt().isAfter(now));

        // ✅ NUEVO (según práctico): al pedir reset se limpia la password
        assertNull(user.getPassword());

        verify(repository, times(1)).save(user);
    }

    // =========================
    // Usuario no encontrado
    // =========================
    @Test
    void execute_ShouldThrowException_WhenUserNotFound() {

        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.empty());

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> requestPasswordResetUserUseCase.execute("correo@padilla.com")
        );

        assertEquals("Usuario no encontrado", exception.getMessage());
        verify(repository, never()).save(any());
    }

    // =========================
    // Usuario no ACTIVE
    // =========================
    @Test
    void execute_ShouldThrowException_WhenUserIsNotActive() {

        LocalDateTime now = LocalDateTime.of(2026, 2, 22, 10, 0);

        User user = User.createNew(
                "correo@padilla.com",
                "pass123",
                "activationCode123",
                now.plusDays(1),
                now
        );

        // Usuario sigue PENDING, no ACTIVE
        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.of(user));

        // Ejecutar UseCase
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> requestPasswordResetUserUseCase.execute("correo@padilla.com")
        );

        assertEquals("Solo usuarios ACTIVE pueden solicitar reset de contraseña", exception.getMessage());

        verify(repository, never()).save(any());
    }

    // =========================
    // Email vacío
    // =========================
    @Test
    void execute_ShouldThrowException_WhenEmailIsBlank() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> requestPasswordResetUserUseCase.execute("")
        );

        assertEquals("El correo electrónico no puede estar en blanco", exception.getMessage());

        verify(repository, never()).save(any());
    }

    // =========================
    // Email nulo
    // =========================
    @Test
    void execute_ShouldThrowException_WhenEmailIsNull() {

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> requestPasswordResetUserUseCase.execute(null)
        );

        assertEquals("El correo electrónico no puede estar en blanco", exception.getMessage());

        verify(repository, never()).save(any());
    }
}
