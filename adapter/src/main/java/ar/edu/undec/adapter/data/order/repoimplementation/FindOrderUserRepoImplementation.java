package ar.edu.undec.adapter.data.order.repoimplementation;

import ar.edu.undec.adapter.data.user.crud.UserCRUD;
import ar.edu.undec.adapter.data.user.mapper.UserMapper;
import ar.edu.undec.core.order.repository.FindUserRepository;
import ar.edu.undec.core.user.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class FindOrderUserRepoImplementation implements FindUserRepository{

    private final UserCRUD userCRUD;

    public FindOrderUserRepoImplementation(UserCRUD userCRUD) {
        this.userCRUD = userCRUD;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userCRUD.findById(id).map(UserMapper::mapEntityToCore);
    }
}
