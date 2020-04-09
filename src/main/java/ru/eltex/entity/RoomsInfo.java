package ru.eltex.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Класс сущности Rooms info.
 */
@Data
@Entity
@Table(name = "roomsinfo")
public class RoomsInfo {
    /**
     * Поле id сообщения (для удовлетворения потребностей БД в уникальном ключе).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idRoomInfo;
    /**
     * Поле номер комнаты.
     */
    private Long number;
    /**
     * Поле, в котором содержится список доступных комнат игроку.
     * Отображается в виде последовательности строк, содержащих информацию о комнатах и оформленных в виде кнопочек
     * на странице /playrooms.
     */
    private String info;
}
