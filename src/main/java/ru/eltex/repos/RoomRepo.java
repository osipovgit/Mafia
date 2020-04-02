package ru.eltex.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.eltex.entity.GameRooms;

import java.util.List;

public interface RoomRepo extends JpaRepository<GameRooms, Long> {
    GameRooms findTopByNumber(Long number);

    GameRooms findByNumberAndUserId(Long number, Long userId);

    List<GameRooms> findAllByNumber(Long number);

    @Modifying
    @Transactional
    void deleteAllByNumber(@Param("number") Long number);

    @Modifying
    @Transactional
    void deleteByNumberAndUserId(@Param("number") Long number, @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set timer = :timer where number =:number")
    void updateDate(@Param("number") Long number, @Param("timer") Long timer);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set phase = :phase where number =:number")
    void updatePhase(@Param("number") Long number, @Param("phase") Integer phase);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set stageOne = :stageOne where number =:number")
    void updateStage(@Param("number") Long number, @Param("stageOne") Boolean stageOne);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set vote = :vote where number =:number and userId =:userId")
    void setVoiceOn(@Param("number") Long number, @Param("vote") Integer vote, @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set done_move = :done_move where number =:number and userId =:userId")
    void setDoneMoveReverse(@Param("number") Long number, @Param("done_move") Boolean done_move, @Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set vote = :vote where number =:number")
    void setVoiceZero(@Param("number") Long number, @Param("vote") Integer vote);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set done_move = :done_move where number =:number")
    void setDoneMoveFalse(@Param("number") Long number, @Param("done_move") Boolean done_move);
}