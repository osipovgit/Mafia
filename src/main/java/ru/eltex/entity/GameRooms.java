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
    @Getter
    @Setter
    private Long id_room;

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

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User_GameRooms> idUser;

}
