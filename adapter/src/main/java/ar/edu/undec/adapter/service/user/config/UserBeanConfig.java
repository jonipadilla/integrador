package ar.edu.undec.adapter.service.user.config;

import ar.edu.undec.core.time.TimeProvider;
import ar.edu.undec.core.user.input.ActivateUserInput;
import ar.edu.undec.core.user.input.ExportUsersForPDFInput;
import ar.edu.undec.core.user.input.RegisterUserInput;
import ar.edu.undec.core.user.input.RequestPasswordResetUserInput;
import ar.edu.undec.core.user.input.ResetPasswordUserInput;
import ar.edu.undec.core.user.repository.ActivateUserRepository;
import ar.edu.undec.core.user.repository.FindUserRepository;
import ar.edu.undec.core.user.repository.RegisterUserRepository;
import ar.edu.undec.core.user.repository.ResetPasswordUserRepository;
import ar.edu.undec.core.user.usecase.ActivateUserUseCase;
import ar.edu.undec.core.user.usecase.ExportUsersForPDFUseCase;
import ar.edu.undec.core.user.usecase.RegisterUserUseCase;
import ar.edu.undec.core.user.usecase.RequestPasswordResetUserUseCase;
import ar.edu.undec.core.user.usecase.ResetPasswordUserUseCase;
import ar.edu.undec.core.utils.input.PDFGeneratorInput;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class UserBeanConfig {

    @Bean
    public RegisterUserInput registerUserInput(RegisterUserRepository repository,
                                               TimeProvider timeProvider) {
        return new RegisterUserUseCase(repository, timeProvider);
    }

    @Bean
    public ActivateUserInput activateUserInput(ActivateUserRepository repository,
                                               TimeProvider timeProvider) {
        return new ActivateUserUseCase(repository, timeProvider);
    }

    @Bean
    public RequestPasswordResetUserInput requestPasswordResetUserInput(ResetPasswordUserRepository repository,
                                                                       TimeProvider timeProvider) {
        return new RequestPasswordResetUserUseCase(repository, timeProvider);
    }

    @Bean
    public ResetPasswordUserInput resetPasswordUserInput(ResetPasswordUserRepository repository,
                                                         TimeProvider timeProvider) {
        return new ResetPasswordUserUseCase(repository, timeProvider);
    }

    //EXPORT PDF
    @Bean
    public ExportUsersForPDFInput exportUsersForPDFInput(FindUserRepository repository,
                                                         PDFGeneratorInput pdfGeneratorInput) {
        return new ExportUsersForPDFUseCase(repository, pdfGeneratorInput);
    }
}
