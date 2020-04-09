package ru.eltex.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eltex.entity.User;

/**
 * Интерфейс User repository.
 */
public interface UserRepo extends JpaRepository<User, Long> {
    /**
     * Find by username or id user.
     *
     * @param username the username
     * @param id       the id
     * @return the user
     */
    User findByUsernameOrId(String username, Long id);
}
