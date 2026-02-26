package ar.edu.undec.adapter.service.user.job;


import ar.edu.undec.adapter.data.user.crud.UserCRUD;
import ar.edu.undec.adapter.data.user.entity.UserEntity;
import ar.edu.undec.core.time.TimeProvider;
import ar.edu.undec.core.user.model.UserStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component


public class ActivationCleanupJob {


    private final UserCRUD userCRUD;
    private final TimeProvider timeProvider;

    public ActivationCleanupJob(UserCRUD userCRUD, TimeProvider timeProvider) {
        this.userCRUD = userCRUD;
        this.timeProvider = timeProvider;
    }

    // Cada 1 hora (idempotente)
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void cleanupExpiredActivations() {

        LocalDateTime now = timeProvider.now();

        List<UserEntity> expired = userCRUD
                .findByStatusAndActivationExpiresAtBefore(UserStatus.PENDING, now);

        for (UserEntity user : expired) {
            //idempotencia: si ya está limpio, no hace nada
            if (user.getActivationCode() != null || user.getActivationExpiresAt() != null) {
                user.setActivationCode(null);
                user.setActivationExpiresAt(null);
                userCRUD.save(user);
            }
        }
    }
}
