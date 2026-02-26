package ar.edu.undec.adapter.data.user.crud;


import ar.edu.undec.adapter.data.user.entity.UserEntity;
import ar.edu.undec.core.user.model.UserStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserCRUD extends CrudRepository<UserEntity, Long>{

    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);


    // Método para Scheduled Job

    List<UserEntity> findByStatusAndActivationExpiresAtBefore(
            UserStatus status,
            LocalDateTime time
    );
}
