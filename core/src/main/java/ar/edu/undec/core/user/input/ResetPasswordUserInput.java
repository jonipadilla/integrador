package ar.edu.undec.core.user.input;

public interface ResetPasswordUserInput {

    void execute(String email,
                 String resetCode,
                 String newPassword);
}
