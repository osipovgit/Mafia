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
    private Integer id;

    @Getter @Setter
    private Integer number;

//    @Getter @Setter
//    private
}
