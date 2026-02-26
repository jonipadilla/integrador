package ar.edu.undec.core.usecasetest;


import ar.edu.undec.core.exceptions.InvalidStateException;
import ar.edu.undec.core.exceptions.ValidationException;
import ar.edu.undec.core.time.TimeProvider;
import ar.edu.undec.core.user.input.UserDTO;
import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.model.UserStatus;
import ar.edu.undec.core.user.repository.ActivateUserRepository;
import ar.edu.undec.core.user.usecase.ActivateUserUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ActivateUserUseCaseTest {

    @Mock
    private ActivateUserRepository repository;

    @Mock
    private TimeProvider timeProvider;

    @InjectMocks
    private ActivateUserUseCase activateUserUseCase;


    //Activación correcta
    @Test
    void execute_ShouldActivateUser_WhenDataIsValid() {

        LocalDateTime now = LocalDateTime.now();

        User user = User.createNew(
                "correo@padilla.com",
                "pass123",
                "codigo123",
                now.plusDays(1),
                now
        );

        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.of(user));

        when(timeProvider.now()).thenReturn(now);

        UserDTO result = activateUserUseCase.execute(
                "correo@padilla.com",
                "codigo123"
        );

        assertEquals(UserStatus.ACTIVE, result.getStatus());
        verify(repository, times(1)).save(user);
    }


    //Usuario no encontrado
    @Test
    void execute_ShouldThrowException_WhenUserNotFound() {

        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.empty());

        assertThrows(ValidationException.class,
                () -> activateUserUseCase.execute("correo@padilla.com", "codigo123"));

        verify(repository, never()).save(any());
    }


    //Código inválido
    @Test
    void execute_ShouldThrowException_WhenCodeIsInvalid() {

        LocalDateTime now = LocalDateTime.now();

        User user = User.createNew(
                "correo@padilla.com",
                "pass123",
                "codigo123",
                now.plusDays(1),
                now
        );

        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.of(user));

        when(timeProvider.now()).thenReturn(now);

        assertThrows(ValidationException.class,
                () -> activateUserUseCase.execute("correo@padilla.com", "codigoIncorrecto"));

        verify(repository, never()).save(any());
    }


    //Código vencido
    @Test
    void execute_ShouldThrowException_WhenCodeIsExpired() {

        LocalDateTime now = LocalDateTime.now();

        User user = User.createNew(
                "correo@padilla.com",
                "pass123",
                "codigo123",
                now.minusDays(1),   // vencido
                now
        );

        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.of(user));

        when(timeProvider.now()).thenReturn(now);

        assertThrows(IllegalStateException.class,
                () -> activateUserUseCase.execute("correo@padilla.com", "codigo123"));

        verify(repository, never()).save(any());
    }


    //Usuario no está en PENDING
    @Test
    void execute_ShouldThrowException_WhenUserIsNotPending() {

        LocalDateTime now = LocalDateTime.now();

        User user = User.createNew(
                "correo@padilla.com",
                "pass123",
                "codigo123",
                now.plusDays(1),
                now
        );

        // activo correctamente usando el nuevo método
        user.activate("codigo123", now);

        when(repository.findByEmail("correo@padilla.com"))
                .thenReturn(Optional.of(user));

        when(timeProvider.now()).thenReturn(now);

        assertThrows(IllegalStateException.class,
                () -> activateUserUseCase.execute("correo@padilla.com", "codigo123"));

        verify(repository, never()).save(any());
    }

}
