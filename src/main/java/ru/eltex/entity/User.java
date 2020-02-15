package ru.eltex.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Класс представления пользователя
 * @author Evgesha
 * @version v1.0
 */
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /** Поле идентификатора */
    @Getter @Setter private Integer id;
    /** Поле имя */
    @Getter @Setter private String name;
    /** Поле роль */
    @Getter @Setter private String destiny;
    /** Поле количество игр */
    @Getter @Setter private Integer countGame;

    /**
     * Метод преобразования пользователя в CSV формат
     * @return возвращает пользователя в формате CSV
     */
    public String toCSV() {
        return this.id + ":" + this.name + ":" + this.countGame;
    }

}
