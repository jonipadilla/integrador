package ar.edu.undec.adapter.data.user.repoimplementation;

import ar.edu.undec.adapter.data.user.crud.UserCRUD;
import ar.edu.undec.adapter.data.user.entity.UserEntity;
import ar.edu.undec.adapter.data.user.mapper.UserMapper;
import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.repository.ResetPasswordUserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ResetPasswordUserRepoImplementation implements ResetPasswordUserRepository{


    private final UserCRUD userCRUD;

    public ResetPasswordUserRepoImplementation(UserCRUD userCRUD) {
        this.userCRUD = userCRUD;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userCRUD.findByEmail(email)
                .map(UserMapper::mapEntityToCore);
    }

    @Override
    public void save(User user) {
        UserEntity saved = userCRUD.save(UserMapper.mapCoreToEntity(user));

        // Si por algún motivo llega sin id (poco probable acá), lo asignamos
        if (user.getId() == null && saved.getId() != null) {
            user.assignId(saved.getId());
        }
    }
}
