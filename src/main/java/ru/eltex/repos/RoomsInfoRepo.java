package ru.eltex.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.eltex.entity.RoomsInfo;

import java.util.List;

/**
 * Интерфейс Rooms info repository.
 */
public interface RoomsInfoRepo extends JpaRepository<RoomsInfo, Long> {

    @Override
    List<RoomsInfo> findAll();

    /**
     * Find by number rooms info.
     *
     * @param number the number
     * @return the rooms info
     */
    RoomsInfo findByNumber(@Param("number") Long number);
}
