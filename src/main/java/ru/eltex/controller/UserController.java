package ru.eltex.controller;

import org.springframework.web.bind.annotation.*;
import ru.eltex.entity.User;

/**
 * Класс-контроллер
 * @author Evgesha
 * @version v1.0
 */
@RestController
public class UserController {

    /**
     * Метод для сопоставления с точкой входа <b>/get_user</b>
     * @param id - User#id пользователя
     * @return Объект пользователя
     * @see User#User()
     */
//    @RequestMapping(value = "/get_user")
//    public User getUser(@RequestParam("id") Integer id) {
//        System.out.println(id);
//        return ;
//    }

//    @RequestMapping("/get_user/{id}")
//    public User getUser1(@PathVariable("id") Integer id) {
//        System.out.println(id);
//        return new User(1, "Boris", "", 939399393);
//    }

}
