package ru.eltex.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roomsinfo")
public class RoomsInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idRoomInfo;
    private Long number;
    private String info;
}
