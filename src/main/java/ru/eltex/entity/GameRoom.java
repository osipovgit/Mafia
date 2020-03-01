package ru.eltex.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * The type Game room.
 */
@Data
@Entity
@Table(name = "room")
public class GameRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @Setter
    private Integer id_room;

    @Getter
    @Setter
    private Integer number;
    /**
     * Поле роль
     */
    @Getter
    @Setter
    private String role;
    /**
     * Поле кол-во голосов против игрока
     */
    @Getter
    @Setter
    private Integer vote;
    /**
     * Поле выбор мафии
     */
    @Getter
    @Setter
    private Boolean mafiaChoice;
    /**
     * Поле выбор доктора
     */
    @Getter
    @Setter
    private Boolean docChoice;
    /**
     * Поле выбор мадамы
     */
    @Getter
    @Setter
    private Boolean girlChoice;


}
