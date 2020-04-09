package ru.eltex.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Класс сущности Game rooms.
 */
@Data
@Entity
@Table(name = "rooms")
public class GameRooms {
    /**
     * Поле id комнаты (для удовлетворения потребностей БД в уникальном ключе).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_room;
    /**
     * Поле номер (детской) комнаты (милиции).
     */
    private Long number;
    /**
     * Поле id пользователя.
     */
    private Long userId;
    /**
     * Поле хост БД.
     */
    private Long hostId;
    /**
     * Поле роль.
     */
    private String role;
    /**
     * Поле кол-во голосов против игрока.
     */
    private Integer vote;
    /**
     * Поле выбор мафии.
     * — Как мне понять, что он выбрал меня?
     * — Он попытается тебя убить.
     */
    private Integer mafiaChoice;
    /**
     * Поле выбор доктора.
     * — Мы бюджетная организация, мы исторически имеем право хамить нашим клиентам.
     */
    private Boolean docChoice;
    /**
     * Поле выбор дамы.
     * [ Путана, путана, путана,
     * Ночная бабочка, ну кто же виноват?!
     * Путана, путана, путана,
     * Огни отелей так заманчиво горят.
     * (c) Олег Газманов]
     */
    private Boolean girlChoice;
    /**
     * Поле таймер для различных игровых моментов.
     */
    private Long timer;
    /**
     * Поле фазы игры.
     */
    private Integer phase;
    /**
     * Поле для пропуска фазы голосования на первом круге игры.
     */
    private Boolean stageOne;
    /**
     * Поле совершенного действия (голос, выбор).
     */
    private Boolean done_move;
}
