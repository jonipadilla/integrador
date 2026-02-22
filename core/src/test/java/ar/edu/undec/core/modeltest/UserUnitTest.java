package ar.edu.undec.core.modeltest;

import ar.edu.undec.core.exceptions.ValidationException;
import ar.edu.undec.core.user.model.User;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserUnitTest {

    @Test
    void createNewUser_ShouldThrowException_WhenEmailIsBlank() {

        LocalDateTime now = LocalDateTime.now();

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> User.createNew(
                        "",
                        "pass123",
                        "codigo123",
                        now.plusDays(1),
                        now
                )
        );

        assertEquals("El correo electrónico no puede estar en blanco",
                exception.getMessage());
    }

    @Test
    void createNewUser_ShouldThrowException_WhenPasswordIsBlank() {

        LocalDateTime now = LocalDateTime.now();

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> User.createNew(
                        "correo@padilla.com",
                        "",
                        "codigo123",
                        now.plusDays(1),
                        now
                )
        );

        assertEquals("La contraseña no puede estar en blanco",
                exception.getMessage());
    }
}
