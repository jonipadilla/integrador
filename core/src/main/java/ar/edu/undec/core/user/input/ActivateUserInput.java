package ar.edu.undec.core.user.input;

public interface ActivateUserInput {

    UserDTO execute(String email, String activationCode);
}
