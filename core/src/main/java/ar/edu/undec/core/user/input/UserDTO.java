package ar.edu.undec.core.user.input;

import ar.edu.undec.core.user.model.UserStatus;

import java.time.LocalDateTime;

public class UserDTO {

    private String email;
    private UserStatus status;
    private LocalDateTime createdAt;

    public UserDTO() {
    }

    public UserDTO(String email, UserStatus status, LocalDateTime createdAt) {
        this.email = email;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}

