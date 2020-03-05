package ru.eltex.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.eltex.entity.GameRooms;

public interface RoomRepo extends JpaRepository<GameRooms, Long> {
    GameRooms findByUsers(String username);
}
