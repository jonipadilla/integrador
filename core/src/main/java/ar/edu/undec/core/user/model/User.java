package ar.edu.undec.core.user.model;

import ar.edu.undec.core.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {


    private Long id;
    private String email;
    private String password;
    private UserStatus status;

    private String activationCode;
    private LocalDateTime activationExpiresAt;

    private String resetCode;
    private LocalDateTime resetExpiresAt;

    private LocalDateTime createdAt;

    // =========================
    // Constructor privado
    // =========================
    private User(Long id,
                 String email,
                 String password,
                 UserStatus status,
                 String activationCode,
                 LocalDateTime activationExpiresAt,
                 String resetCode,
                 LocalDateTime resetExpiresAt,
                 LocalDateTime createdAt) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.status = status;
        this.activationCode = activationCode;
        this.activationExpiresAt = activationExpiresAt;
        this.resetCode = resetCode;
        this.resetExpiresAt = resetExpiresAt;
        this.createdAt = createdAt;
    }

    // =========================
    // Factory method
    // =========================
    public static User createNew(String email,
                                 String password,
                                 String activationCode,
                                 LocalDateTime activationExpiresAt,
                                 LocalDateTime now) {

        if (email == null || email.isBlank())
            throw new ValidationException("El correo electrónico no puede estar en blanco");

        if (password == null || password.isBlank())
            throw new ValidationException("La contraseña no puede estar en blanco");

        if (activationCode == null || activationCode.isBlank())
            throw new ValidationException("El código de activación no puede estar en blanco");

        if (activationExpiresAt == null)
            throw new ValidationException("La fecha de expiración no puede ser nula");

        if (now == null)
            throw new ValidationException("La fecha actual no puede ser nula");

        return new User(
                null,
                email,
                password,
                UserStatus.PENDING,
                activationCode,
                activationExpiresAt,
                null,
                null,
                now
        );
    }

    // =========================
    // ACTIVACIÓN
    // =========================
    public void activate(String code, LocalDateTime now) {

        if (status != UserStatus.PENDING)
            throw new IllegalStateException("El usuario no está pendiente de activación");

        if (code == null || code.isBlank())
            throw new ValidationException("El código de activación no puede estar en blanco");

        if (!Objects.equals(this.activationCode, code))
            throw new ValidationException("Código de activación inválido");

        if (activationExpiresAt == null || activationExpiresAt.isBefore(now))
            throw new IllegalStateException("El código de activación está vencido");

        this.status = UserStatus.ACTIVE;
        this.activationCode = null;
        this.activationExpiresAt = null;
    }

    // =========================
    // SOLICITAR RESET
    // =========================
    public void requestPasswordReset(String resetCode,
                                     LocalDateTime expiration) {

        if (status != UserStatus.ACTIVE)
            throw new IllegalStateException("Solo usuarios ACTIVE pueden solicitar reset");

        if (resetCode == null || resetCode.isBlank())
            throw new ValidationException("El código de reset no puede estar en blanco");

        if (expiration == null)
            throw new ValidationException("La fecha de expiración no puede ser nula");

        this.password = null;
        this.resetCode = resetCode;
        this.resetExpiresAt = expiration;
        this.status = UserStatus.RESET;
    }

    // =========================
    // RESET PASSWORD
    // =========================
    public void resetPassword(String resetCode,
                              String newPassword,
                              LocalDateTime now) {

        if (status != UserStatus.RESET)
            throw new IllegalStateException("El usuario no está en estado RESET");

        if (!Objects.equals(this.resetCode, resetCode))
            throw new ValidationException("Código de reset inválido");

        if (resetExpiresAt == null || resetExpiresAt.isBefore(now))
            throw new IllegalStateException("El código de reset está vencido");

        if (newPassword == null || newPassword.isBlank())
            throw new ValidationException("La nueva contraseña no puede estar en blanco");

        this.password = newPassword;
        this.resetCode = null;
        this.resetExpiresAt = null;
        this.status = UserStatus.ACTIVE;
    }


    // Factory desde persistencia
// =========================
    public static User factoryFromEntity(Long id,
                                         String email,
                                         String password,
                                         UserStatus status,
                                         String activationCode,
                                         LocalDateTime activationExpiresAt,
                                         String resetCode,
                                         LocalDateTime resetExpiresAt,
                                         LocalDateTime createdAt) {

        if (id == null)
            throw new ValidationException("El id no puede ser nulo");

        if (email == null || email.isBlank())
            throw new ValidationException("El correo electrónico no puede estar en blanco");

        if (status == null)
            throw new ValidationException("El estado no puede ser nulo");

        if (createdAt == null)
            throw new ValidationException("La fecha de creación no puede ser nula");

        return new User(
                id,
                email,
                password,
                status,
                activationCode,
                activationExpiresAt,
                resetCode,
                resetExpiresAt,
                createdAt
        );
    }

    // =========================
    // GETTERS
    // =========================

    public Long getId() { return id; }

    public String getEmail() { return email; }

    public String getPassword() { return password; }

    public UserStatus getStatus() { return status; }

    public String getActivationCode() { return activationCode; }

    public LocalDateTime getActivationExpiresAt() { return activationExpiresAt; }

    public String getResetCode() { return resetCode; }

    public LocalDateTime getResetExpiresAt() { return resetExpiresAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    // =========================
    // Infraestructura
    // =========================
    public void assignId(Long id) {
        if (this.id != null)
            throw new IllegalStateException("El usuario ya tiene ID asignado");
        this.id = id;
    }
}
