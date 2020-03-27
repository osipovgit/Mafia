package ru.eltex.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.eltex.entity.GameRooms;

public interface RoomRepo extends JpaRepository<GameRooms, Long> {
    GameRooms findByNumber(Long number);

    GameRooms findAllByNumber(Long number);

    @Query(value = "delete from GameRooms where 'number'=:room_number")
    public void Delete(Long room_number);
}
