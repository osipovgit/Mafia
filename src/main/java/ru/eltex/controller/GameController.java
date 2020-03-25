package ru.eltex.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.eltex.entity.GameRooms;
import ru.eltex.entity.User;
import ru.eltex.repos.RoomRepo;

import java.util.List;

/**
 * Класс-контроллер
 *
 * @author Evgesha
 * @version v1.0
 */
@RestController
@RequestMapping("/playrooms")
public class GameController {

    private long roomNumber = 0;

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
    @PostMapping
    public GameRooms createRoom(User user) {
        GameRooms gameRooms = new GameRooms();
        gameRooms.setUsers(user);
        gameRooms.setHostId(user.getId());
        gameRooms.setNumber(++roomNumber);
        roomRepo.save(gameRooms);
        return gameRooms;
    }

//    @RequestMapping("/get_user/{id}")
//    public User getUser1(@PathVariable("id") Integer id) {
//        System.out.println(id);
//        return new User(1, "Boris", "", 939399393);
//    }


}
