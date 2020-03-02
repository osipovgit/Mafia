package ru.eltex.entity;


import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

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
    @Getter
    @Setter
    private Long id;
    /**
     * Поле имя
     */
    @Getter
    @Setter
    private String username;
    /**
     * Поле пароля
     */
    @Getter
    @Setter
    private String password;
    /**
     * Поле статуса игрока
     */
    @Getter
    @Setter
    private Boolean active;
    /**
     * Поле готовности (default = false)
     */
    @Getter
    @Setter
    private Boolean ready;
    /**
     * Поле количество игр
     */
    @Getter
    @Setter
    private Long countGame;

    public User() {
    }

    public User(String username, String password, Boolean active, Boolean ready) {
          this.username = username;
          this.password = password;
          this.active = active;
          this.ready = ready;
    }
}
// TODO Кажется самое время подумать над таблицами и все учесть, займись блять
