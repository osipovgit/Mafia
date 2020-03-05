package ru.eltex.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

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
    private Integer number;
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
     * Поле list пользователей, находящихся в комнате №number
     */
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;
    /**
     * Поле хост БД
     * Так и не смог пройти фейсконтроль в БД
     */
    @Transient
    private Integer hostId;

}
