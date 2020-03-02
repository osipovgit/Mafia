package ru.eltex.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eltex.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
