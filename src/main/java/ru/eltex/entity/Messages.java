package ru.eltex.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Класс сущности Messages.
 * Для отображения собщений в чатах внутри комнат.
 */
@Data
@Entity
@Table(name = "messages")
public class Messages {
    /**
     * Поле id сообщения (для удовлетворения потребностей БД в уникальном ключе).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Поле номер комнаты.
     */
    private Long roomNumber;
    /**
     * Поле с текстом сообщения.
     */
    private String message;
}
