package ar.edu.undec.core.user.input;


public interface RegisterUserInput {

    UserDTO execute(String email, String password);
}
