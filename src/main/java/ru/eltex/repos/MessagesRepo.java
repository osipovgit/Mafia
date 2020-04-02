package ru.eltex.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.eltex.entity.Messages;

import java.util.List;

public interface MessagesRepo extends JpaRepository<Messages, Long> {

    List<Messages> findAllByRoomNumberOrderById(Long roomNumber);

    @Modifying
    @Transactional
    void deleteAllByRoomNumber(@Param("roomNumber") Long roomNumber);
}
