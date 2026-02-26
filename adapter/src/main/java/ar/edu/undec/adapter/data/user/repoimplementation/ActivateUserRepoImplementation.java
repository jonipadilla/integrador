package ar.edu.undec.adapter.data.user.repoimplementation;


import ar.edu.undec.adapter.data.user.crud.UserCRUD;
import ar.edu.undec.adapter.data.user.entity.UserEntity;
import ar.edu.undec.adapter.data.user.mapper.UserMapper;
import ar.edu.undec.core.user.repository.ActivateUserRepository;
import ar.edu.undec.core.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public class ActivateUserRepoImplementation implements ActivateUserRepository {


    private final UserCRUD userCRUD;

    public ActivateUserRepoImplementation(UserCRUD userCRUD) {
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

        if (user.getId() == null && saved.getId() != null) {
            user.assignId(saved.getId());
        }
    }
}
