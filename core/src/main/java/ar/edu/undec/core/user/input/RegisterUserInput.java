package ar.edu.undec.core.user.input;

import ar.edu.undec.core.user.model.User;

public interface RegisterUserInput {

    UserDTO execute(String email, String password);
}
