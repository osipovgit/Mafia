package ru.eltex.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.eltex.entity.RoomsInfo;

import java.util.List;

public interface RoomsInfoRepo extends JpaRepository<RoomsInfo, Long> {

    @Override
    List<RoomsInfo> findAll();

    RoomsInfo findByNumber(@Param("number") Long number);

    @Modifying
    @Transactional
    void deleteAllByNumber(@Param("number") Long number);
}
