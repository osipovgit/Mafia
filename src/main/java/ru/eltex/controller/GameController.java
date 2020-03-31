package ru.eltex.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.eltex.entity.GameRooms;
import ru.eltex.entity.User;
import ru.eltex.repos.RoomRepo;
import ru.eltex.repos.UserRepo;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    @GetMapping("/create")
    public String createRoom(HttpServletRequest request, Model model) {
        GameRooms gameRooms = new GameRooms();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
        gameRooms.setUsers(userRepoById);
        gameRooms.setHostId(Long.parseLong(cookies[0].getValue()));
        gameRooms.setNumber(++roomNumber);
        roomRepo.save(gameRooms);
        System.out.println("Создана комната: " + gameRooms.getNumber());
        return "/playrooms/" + gameRooms.getNumber() + "";
    }

    @GetMapping("/update")
    public String viewAllRooms(HttpServletRequest request, Model model) {
        return "";
    }

    @GetMapping("/{roomNumber}/delete")
    public String deleteRoom(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        System.out.println("Удалена комната номер: " + roomNumber);
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
        GameRooms gameRooms = roomRepo.findByNumber(roomNumber);
        if (userRepoById.getId().equals(gameRooms.getHostId())) {       //TODO: Think about condition
            roomRepo.deleteAllByNumber(roomNumber);
        }
        return "/playrooms";
    }

    @GetMapping("/{roomNumber}/start")
    public String startGame(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {

        return "";
    }

    @GetMapping("/{roomNumber}/update_view_players")
    public String playersInRoom(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        List<GameRooms> rooms = roomRepo.findAllByNumber(roomNumber);
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(rooms);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(jsonStr);
        return jsonStr;
    }

    @GetMapping("/{roomNumber}/game")
    public String gameMode(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {

        return "";
    }


}
/*
{
    "messages": [{"message" : .getUsername() + ": " + "привет дарагие патпищики"},
                 {"message" : "Lanturn: @$%!* иди"},
                 {"message" : "Boris: у вас осталось шесть дней"},
                 {"message" : "Lanturn: *!%$@ идем мы походу (ред.)"},
                 {"message" : "*Биба и Боба покидают чат*"}
                ]
}
 */