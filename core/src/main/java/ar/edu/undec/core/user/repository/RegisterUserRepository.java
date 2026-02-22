package ar.edu.undec.core.user.repository;

import ar.edu.undec.core.user.model.User;

public interface RegisterUserRepository {

    /**
     * Verifica si existe un usuario con el email dado
     * @param email Email a verificar
     * @return true si ya existe, false si no
     */
    boolean existsByEmail(String email);

    /**
     * Persiste el usuario en el almacenamiento
     * @param user Usuario a guardar
     * @return Usuario guardado (opcional, puede ser el mismo)
     */
    User save(User user);
}
