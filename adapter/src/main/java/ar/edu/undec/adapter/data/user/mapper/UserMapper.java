package ar.edu.undec.adapter.data.user.mapper;

import ar.edu.undec.adapter.data.user.entity.UserEntity;
import ar.edu.undec.core.user.model.User;


public class UserMapper {

    public static UserEntity mapCoreToEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getStatus(),
                user.getActivationCode(),
                user.getActivationExpiresAt(),
                user.getResetCode(),
                user.getResetExpiresAt(),
                user.getCreatedAt()
        );
    }

    public static User mapEntityToCore(UserEntity userEntity) {
        return User.factoryFromEntity(
                userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getStatus(),
                userEntity.getActivationCode(),
                userEntity.getActivationExpiresAt(),
                userEntity.getResetCode(),
                userEntity.getResetExpiresAt(),
                userEntity.getCreatedAt()
        );
    }
}
