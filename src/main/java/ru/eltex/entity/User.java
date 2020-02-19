package ru.eltex.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Класс представления пользователя
 *
 * @author Evgesha
 * @version v1.0
 */
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * Поле идентификатора
     */
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Integer id;
    /**
     * Поле имя
     */
    @Getter
    @Setter
    private String name;
    /**
     * Поле роль
     */
    @Getter
    @Setter
    private String destiny;
    /**
     * Поле готовности (default = 0)
     */
    @Getter
    @Setter
    private Integer ready = 0;
    /**
     * Поле статуса игрока (в игре/выбыл) (default = 1)
     */
    @Getter
    @Setter
    private Integer alive = 1;
    /**
     * Поле количество игр
     */
    @Getter
    @Setter
    private Integer countGame;

    /**
     * Метод преобразования пользователя в CSV формат
     *
     * @return возвращает пользователя в формате CSV
     */
    public String toCSV() {
        return this.id + ":" + this.name + ":" + this.destiny + ":" + this.ready + ":" + this.alive + ":" + this.countGame;
    }

}
