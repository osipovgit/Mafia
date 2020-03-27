package ru.eltex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.eltex.entity.GameRooms;
import ru.eltex.entity.User;
import ru.eltex.repos.RoomRepo;
import ru.eltex.repos.UserRepo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private UserRepo userRepo;

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
    public String createRoom(HttpServletRequest request, Model model) {
        GameRooms gameRooms = new GameRooms();
        Cookie[] cookies = request.getCookies();
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[1].getValue()));
        gameRooms.setUsers(userRepoById);
        gameRooms.setHostId(Long.parseLong(cookies[1].getValue()));
        gameRooms.setNumber(++roomNumber);
        roomRepo.save(gameRooms);
        return "redirect:/playrooms/" + gameRooms.getNumber();
    }

    //    @DeleteMapping
    public String deleteRoom(Long room_number, HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[1].getValue()));
        GameRooms gameRooms = roomRepo.findByNumber(room_number);
        if (userRepoById.getId().equals(gameRooms.getHostId())) {       //TODO: Think about condition
            roomRepo.Delete(room_number);
        }
        return "redirect:/playrooms";
    }

//    @RequestMapping("/get_user/{id}")
//    public User getUser1(@PathVariable("id") Integer id) {
//        System.out.println(id);
//        return new User(1, "Boris", "", 939399393);
//    }


}
