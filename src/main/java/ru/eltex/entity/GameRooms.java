package ru.eltex.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * The type Game room.
 */
@Data
@Entity
@Table(name = "rooms")
public class GameRooms {
    /**
     * Поле id комнаты (пусть будет, иначе не работает)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_room;
    /**
     * Поле номер детской комнаты милиции
     */
    private Long number;
    /**
     * Поле list пользователей, находящихся в комнате №number
     */
    private Long userId;
    /**
     * Поле хост БД
     */
    private Long hostId;
    /**
     * Поле роль
     */
    private String role;
    /**
     * Поле кол-во голосов против игрока
     */
    private Integer vote;
    /**
     * Поле выбор мафии
     * — Как мне понять, что он выбрал меня?
     * — Он попытается тебя убить.
     */
    private Integer mafiaChoice;
    /**
     * Поле выбор доктора
     */
    private Boolean docChoice;
    /**
     * Поле выбор мадамы
     */
    private Boolean girlChoice;
    /**
     * Поле начала времен
     */
    private Long timer;
    /**
     * Поле фазы игры
     */
    private Integer phase = 1;
    /**
     * Поле начала времен
     */
    private Boolean stageOne;
    /**
     * Поле совершенного действия (голос, выбор)
     */
    private Boolean done_move;
}
