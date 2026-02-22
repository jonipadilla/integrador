package ar.edu.undec.core.user.repository;

import ar.edu.undec.core.user.model.User;

import java.util.Optional;

public interface ResetPasswordUserRepository {

    Optional<User> findByEmail(String email);

    void save(User user);
}
