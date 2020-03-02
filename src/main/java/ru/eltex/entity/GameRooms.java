package ru.eltex.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * The type Game room.
 */
@Data
@Entity
@Table(name = "rooms")
public class GameRooms {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_room;

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
     */
    private Boolean mafiaChoice;
    /**
     * Поле выбор доктора
     */
    private Boolean docChoice;
    /**
     * Поле выбор мадамы
     */
    private Boolean girlChoice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

}
