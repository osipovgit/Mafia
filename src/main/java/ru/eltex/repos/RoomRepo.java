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

    List<GameRooms> findAllByNumber(Long number);

    GameRooms findByNumberAndUserId(Long number, Long userId);

    @Override
    List<GameRooms> findAll();
//    @Modifying
//    @Transactional
//    @Query(nativeQuery = true, value = "select number, count(userId) from rooms group by number")
//    List<GameRooms> getNumberAndCount();

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
    @Query(nativeQuery = true, value = "update rooms set vote = :vote where number =:number and user_id =:user_id")
    void setVoiceOn(@Param("number") Long number, @Param("vote") Integer vote, @Param("user_id") Long user_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set done_move = :done_move where number =:number and user_id =:user_id")
    void setDoneMoveReverse(@Param("number") Long number, @Param("done_move") Boolean done_move, @Param("user_id") Long user_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set vote = :vote where number =:number")
    void setVoteZero(@Param("number") Long number, @Param("vote") Integer vote);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set role = :role where number =:number and user_id =:user_id")
    void setRoles(@Param("number") Long number, @Param("role") String role, @Param("user_id") Long user_id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set done_move = :done_move where number =:number")
    void setDoneMoveFalse(@Param("number") Long number, @Param("done_move") Boolean done_move);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set done_move = :done_move, girl_choice = :girl_choice, doc_choice = :doc_choice, mafia_choice = :mafia_choice  where number =:number")
    void resetAll(@Param("number") Long number, @Param("done_move") Boolean done_move, @Param("girl_choice") Boolean girl_choice, @Param("doc_choice") Boolean doc_choice, @Param("mafia_choice") Integer mafia_choice);
}