package ar.edu.undec.adapter.data.user.repoimplementation;

import ar.edu.undec.adapter.data.user.crud.UserCRUD;

import ar.edu.undec.adapter.data.user.mapper.UserMapper;
import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.repository.RegisterUserRepository;
import org.springframework.stereotype.Repository;

import ar.edu.undec.adapter.data.user.entity.UserEntity;



@Repository
public class RegisterUserRepoImplementation implements RegisterUserRepository {

    private final UserCRUD userCRUD;

    public RegisterUserRepoImplementation(UserCRUD userCRUD) {
        this.userCRUD = userCRUD;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userCRUD.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        UserEntity saved = userCRUD.save(UserMapper.mapCoreToEntity(user));

        if (user.getId() == null && saved.getId() != null) {
            user.assignId(saved.getId());
        }

        return user;
    }
}
