package ru.eltex.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.eltex.entity.GameRooms;

import java.util.List;

/**
 * Интерфейс Room repository.
 */
public interface RoomRepo extends JpaRepository<GameRooms, Long> {
    /**
     * Find top by number game rooms.
     *
     * @param number the number
     * @return the game rooms
     */
    GameRooms findTopByNumber(Long number);

    /**
     * Find all by number list.
     *
     * @param number the number
     * @return the list
     */
    List<GameRooms> findAllByNumber(Long number);

    /**
     * Find all by number order by vote desc list.
     *
     * @param number the number
     * @return the list
     */
    List<GameRooms> findAllByNumberOrderByVoteDesc(Long number);

    /**
     * Find all by number order by mafia choice desc list.
     *
     * @param number the number
     * @return the list
     */
    List<GameRooms> findAllByNumberOrderByMafiaChoiceDesc(Long number);

    /**
     * Find by number and user id game rooms.
     *
     * @param number the number
     * @param userId the user id
     * @return the game rooms
     */
    GameRooms findByNumberAndUserId(Long number, Long userId);

    @Override
    List<GameRooms> findAll();

    /**
     * Delete all by number.
     *
     * @param number the number
     */
    @Modifying
    @Transactional
    void deleteAllByNumber(@Param("number") Long number);

    /**
     * Delete by number and user id.
     *
     * @param number the number
     * @param userId the user id
     */
    @Modifying
    @Transactional
    void deleteByNumberAndUserId(@Param("number") Long number, @Param("userId") Long userId);

    /**
     * Update timer by number.
     *
     * @param number the number
     * @param timer  the timer
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set timer = :timer where number =:number")
    void updateDate(@Param("number") Long number, @Param("timer") Long timer);

    /**
     * Update phase by number.
     *
     * @param number the number
     * @param phase  the phase
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set phase = :phase where number =:number")
    void updatePhase(@Param("number") Long number, @Param("phase") Integer phase);

    /**
     * Update stage by number.
     *
     * @param number    the number
     * @param stage_one the stage one
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set stage_one = :stage_one where number =:number")
    void updateStage(@Param("number") Long number, @Param("stage_one") Boolean stage_one);

    /**
     * Update stage by number and user id.
     *
     * @param number  the number
     * @param vote    the vote
     * @param user_id the user id
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set vote = :vote where number =:number and user_id =:user_id")
    void setVoiceOn(@Param("number") Long number, @Param("vote") Integer vote, @Param("user_id") Long user_id);

    /**
     * Update done_move by number and user id.
     *
     * @param number    the number
     * @param done_move the done move
     * @param user_id   the user id
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set done_move = :done_move where number =:number and user_id =:user_id")
    void setDoneMoveReverse(@Param("number") Long number, @Param("done_move") Boolean done_move, @Param("user_id") Long user_id);

    /**
     * Update vote by number.
     *
     * @param number the number
     * @param vote   the vote
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set vote = :vote where number =:number")
    void setVoteZero(@Param("number") Long number, @Param("vote") Integer vote);

    /**
     * Update role by number and user id.
     *
     * @param number  the number
     * @param role    the role
     * @param user_id the user id
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set role = :role where number =:number and user_id =:user_id")
    void setRoles(@Param("number") Long number, @Param("role") String role, @Param("user_id") Long user_id);

    /**
     * Update role by number.
     *
     * @param number the number
     * @param role   the role
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set role = :role where number =:number")
    void resetRoles(@Param("number") Long number, @Param("role") String role);

    /**
     * Update girl_choice by number and user id.
     *
     * @param number      the number
     * @param girl_choice the girl choice
     * @param user_id     the user id
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set girl_choice = :girl_choice where number =:number and user_id =:user_id")
    void setGirlChoiceOn(@Param("number") Long number, @Param("girl_choice") Boolean girl_choice, @Param("user_id") Long user_id);

    /**
     * Update girl_choice by number.
     *
     * @param number      the number
     * @param girl_choice the girl choice
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set girl_choice = :girl_choice where number =:number")
    void resetGirlChoice(@Param("number") Long number, @Param("girl_choice") Boolean girl_choice);

    /**
     * Update doc_choice by number and user id.
     *
     * @param number     the number
     * @param doc_choice the doc choice
     * @param user_id    the user id
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set doc_choice = :doc_choice where number =:number and user_id =:user_id")
    void setDoctorChoiceOn(@Param("number") Long number, @Param("doc_choice") Boolean doc_choice, @Param("user_id") Long user_id);

    /**
     * Update mafia_choice by number and user id.
     *
     * @param number       the number
     * @param mafia_choice the mafia choice
     * @param user_id      the user id
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set mafia_choice = :mafia_choice where number =:number and user_id =:user_id")
    void setMafiaChoiceOn(@Param("number") Long number, @Param("mafia_choice") Integer mafia_choice, @Param("user_id") Long user_id);

    /**
     * Update done_move by number.
     *
     * @param number    the number
     * @param done_move the done move
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set done_move = :done_move where number =:number")
    void setDoneMoveFalse(@Param("number") Long number, @Param("done_move") Boolean done_move);

    /**
     * Update done_move, doc_choice, mafia_choice by number.
     *
     * @param number       the number
     * @param done_move    the done move
     * @param doc_choice   the doc choice
     * @param mafia_choice the mafia choice
     */
    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "update rooms set done_move = :done_move, doc_choice = :doc_choice, mafia_choice = :mafia_choice  where number =:number")
    void resetAll(@Param("number") Long number, @Param("done_move") Boolean done_move, @Param("doc_choice") Boolean doc_choice, @Param("mafia_choice") Integer mafia_choice);
}