package ar.edu.undec.core.order.repository;

import ar.edu.undec.core.user.model.User;

import java.util.Optional;

public interface FindUserRepository {


    Optional<User> findById(Long id);
}
