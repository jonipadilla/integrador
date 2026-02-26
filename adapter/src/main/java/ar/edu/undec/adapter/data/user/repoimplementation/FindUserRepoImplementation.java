package ar.edu.undec.adapter.data.user.repoimplementation;

import ar.edu.undec.adapter.data.user.crud.UserCRUD;
import ar.edu.undec.adapter.data.user.mapper.UserMapper;
import ar.edu.undec.core.user.model.User;
import ar.edu.undec.core.user.repository.FindUserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository

public class FindUserRepoImplementation implements FindUserRepository{


    private final UserCRUD userCRUD;

    public FindUserRepoImplementation(UserCRUD userCRUD) {
        this.userCRUD = userCRUD;
    }

    @Override
    public List<User> getAllSavedUsers() {
        List<User> users = new ArrayList<>();
        userCRUD.findAll().forEach(entity -> users.add(UserMapper.mapEntityToCore(entity)));
        return users;
    }
}
