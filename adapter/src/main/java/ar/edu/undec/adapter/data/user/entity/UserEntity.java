package ar.edu.undec.adapter.data.user.entity;

import ar.edu.undec.core.user.model.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    @Column(length = 100)
    private String activationCode;

    private LocalDateTime activationExpiresAt;

    @Column(length = 100)
    private String resetCode;

    private LocalDateTime resetExpiresAt;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public UserEntity() {
    }

    public UserEntity(Long id,
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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }

    public String getActivationCode() { return activationCode; }
    public void setActivationCode(String activationCode) { this.activationCode = activationCode; }

    public LocalDateTime getActivationExpiresAt() { return activationExpiresAt; }
    public void setActivationExpiresAt(LocalDateTime activationExpiresAt) { this.activationExpiresAt = activationExpiresAt; }

    public String getResetCode() { return resetCode; }
    public void setResetCode(String resetCode) { this.resetCode = resetCode; }

    public LocalDateTime getResetExpiresAt() { return resetExpiresAt; }
    public void setResetExpiresAt(LocalDateTime resetExpiresAt) { this.resetExpiresAt = resetExpiresAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}