package ru.eltex.entity;


import lombok.Data;

import javax.persistence.*;

/**
 * Класс представления пользователя.
 *
 * @author @osipovgit
 * @version v1.0
 */
@Data
@Entity
@Table(name = "user")
public class User {
    /**
     * Поле идентификатора.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Поле имя.
     */
    private String username;
    /**
     * Поле пароля.
     */
    private String password;
    /**
     * Поле количество игр. Для подведения статистики по итогам игр. [В разработке]
     */
    private Long countGame;
}