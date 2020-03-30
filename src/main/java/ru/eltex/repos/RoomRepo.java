package ru.eltex.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.eltex.entity.GameRooms;

public interface RoomRepo extends JpaRepository<GameRooms, Long> {
    GameRooms findByNumber(Long number);

//    GameRooms findAllByNumber(Long number);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "delete from rooms where number=:room_number")
    void Delete(@Param("room_number") Long room_number);
}