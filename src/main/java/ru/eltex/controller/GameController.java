package ru.eltex.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.eltex.repos.RoomRepo;

/**
 * Класс-контроллер
 *
 * @author Evgesha
 * @version v1.0
 */
@RestController
@RequestMapping("/playrooms")
public class GameController {

    private int roomNumber = 1;

    private final RoomRepo roomRepo;

    public GameController(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

//    /**
//     * Метод для сопоставления с точкой входа <b>/get_user</b>
//     * @param id - User#id пользователя
//     * @return Объект пользователя
//     * @see User#User()
//     */


//    @PostMapping("/create_room")
//    public String createRoom(@RequestParam("id") Long id) {
//        System.out.println(id);
//        return "/playroom/" + roomNumber;
//    }

//    @RequestMapping("/get_user/{id}")
//    public User getUser1(@PathVariable("id") Integer id) {
//        System.out.println(id);
//        return new User(1, "Boris", "", 939399393);
//    }

//    @GetMapping("/{roomNumber}")
//    public GameController roomId(@PathVariable int roomNumber){
//
//    }


}
