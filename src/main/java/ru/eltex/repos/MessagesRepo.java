package ru.eltex.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.eltex.entity.Messages;

import java.util.List;

/**
 * Интерфейс Messages repository.
 */
public interface MessagesRepo extends JpaRepository<Messages, Long> {

    /**
     * Find all by room number order by id list.
     *
     * @param roomNumber the room number
     * @return the list
     */
    List<Messages> findAllByRoomNumberOrderById(Long roomNumber);

    /**
     * Delete all by room number.
     *
     * @param roomNumber the room number
     */
    @Modifying
    @Transactional
    void deleteAllByRoomNumber(@Param("roomNumber") Long roomNumber);
}
