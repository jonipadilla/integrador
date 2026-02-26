package ar.edu.undec.core.user.repository;

import ar.edu.undec.core.user.model.User;
import java.util.List;

public interface FindUserRepository {

    List<User> getAllSavedUsers();
}
