package ru.eltex.entity;


import lombok.Data;

import javax.persistence.*;

/**
 * Класс представления пользователя
 *
 * @author Evgesha
 * @version v1.0
 */
@Data
@Entity
@Table(name = "user")
public class User {
    /**
     * Поле идентификатора
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * Поле имя
     */
    private String username;
    /**
     * Поле пароля
     */
    private String password;
    /**
     * Поле количество игр
     */
    private Long countGame;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}