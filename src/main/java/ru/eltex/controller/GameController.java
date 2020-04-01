package ru.eltex.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.eltex.entity.GameRooms;
import ru.eltex.entity.User;
import ru.eltex.repos.RoomRepo;
import ru.eltex.repos.UserRepo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
        gameRooms.setUserId(userRepoById.getId());
        gameRooms.setHostId(Long.parseLong(cookies[0].getValue()));
        gameRooms.setNumber(++roomNumber);
        Date date = new Date();
        gameRooms.setTimer(date.getTime());
        gameRooms.setStageOne(true);
        gameRooms.setPhase(1);
        roomRepo.save(gameRooms);
        System.out.println("Создана комната: " + gameRooms.getNumber());
        return "/playrooms/" + gameRooms.getNumber();
    }

    @GetMapping("/update")
    public String viewAllRooms(HttpServletRequest request, Model model) {
        return "";
    }

    @GetMapping("/{roomNumber}/joinPlayer")
    public String joinPlayer(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
        List<GameRooms> gameRoomsList = roomRepo.findAllByNumber(roomNumber);
        if (roomRepo.findByNumberAndUserId(roomNumber, userRepoById.getId()) == null && gameRoomsList.size() < 10) {       //TODO: Think about condition
            GameRooms gameRooms = new GameRooms();
            gameRooms.setUserId(userRepoById.getId());
            gameRooms.setHostId(roomRepo.findTopByNumber(roomNumber).getHostId());
            gameRooms.setNumber(roomNumber);
            Date date = new Date();
            gameRooms.setTimer(date.getTime());
            gameRooms.setStageOne(true);
            gameRooms.setPhase(1);
            roomRepo.save(gameRooms);
            System.out.println("Пользователь: " + userRepoById.getUsername() + " присоединяется к комнате: " + roomNumber);
            return "/playrooms" + roomNumber;
        } else return "/playrooms/";
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
        GameRooms gameRooms = roomRepo.findTopByNumber(roomNumber);
        if (userRepoById.getId().equals(gameRooms.getHostId()) && gameRooms.getRole() == null) {       //TODO: Think about condition
            roomRepo.deleteAllByNumber(roomNumber);
            return "/playrooms";
        } else return "/playrooms/" + roomNumber;
    }

    @GetMapping("/{roomNumber}/leave")
    public String leaveRoom(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
        GameRooms gameRooms = roomRepo.findByNumberAndUserId(roomNumber, userRepoById.getId());
        if (userRepoById.getId().equals(gameRooms.getHostId()) && gameRooms.getRole() == null) {       //TODO: Think about condition
            roomRepo.deleteByNumberAndUserId(roomNumber, userRepoById.getId());
            System.out.println("Пользователь: " + userRepoById.getUsername() + " покинул комнату номер: " + roomNumber);
            return "/playrooms";
        } else return "/playrooms/" + roomNumber;
    }

    @GetMapping("/{roomNumber}/start")
    public Long startGame(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        GameRooms gameRoomTop = roomRepo.findTopByNumber(roomNumber);
        List<GameRooms> gameRoomsList = roomRepo.findAllByNumber(roomNumber);
        Date dateNow = new Date();
        if (gameRoomsList.size() <= 1) { // TODO: change to < 5
            roomRepo.updateDate(roomNumber, dateNow.getTime());
            return 0L;
        } else if (dateNow.getTime() - gameRoomTop.getTimer() < 60000 & gameRoomsList.size() < 10) {
            return 1L; //TODO: return timer to begin
        } else {
            roomRepo.updateDate(roomNumber, dateNow.getTime());
            return 2L;
        }
    }

    @GetMapping("/{roomNumber}/chat")
    public String updateChat(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {

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
//        System.out.println(jsonStr);
        return jsonStr;
    }

    @GetMapping("/{roomNumber}/game")
    public String gameMode(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        GameRooms gameRooms = roomRepo.findTopByNumber(roomNumber);
        Date dateNow = new Date();
        String string = "";
        switch (gameRooms.getPhase()) {
            case 1:
                if (dateNow.getTime() - gameRooms.getTimer() >= 120000) {
                    roomRepo.updatePhase(roomNumber, 2);
                    Date date = new Date();
                    roomRepo.updateDate(roomNumber, date.getTime());
                }
                string = "" + (dateNow.getTime() - gameRooms.getTimer()) + " " + gameRooms.getPhase();
                break;
            case 2:
                if (dateNow.getTime() - gameRooms.getTimer() >= 60000) {
                    if (gameRooms.getStageOne().equals(true)) {
                        string = string;
                    } else {
                        roomRepo.updateStage(roomNumber, false);
                    }
                    roomRepo.updatePhase(roomNumber, 3);
                    Date date = new Date();
                    roomRepo.updateDate(roomNumber, date.getTime());
                }
                string = "" + (dateNow.getTime() - gameRooms.getTimer()) + " " + gameRooms.getPhase();
                break;
            case 3:
                if (dateNow.getTime() - gameRooms.getTimer() >= 120000) {
                    roomRepo.updatePhase(roomNumber, 1);
                    Date date = new Date();
                    roomRepo.updateDate(roomNumber, date.getTime());
                }
                string = "" + (dateNow.getTime() - gameRooms.getTimer()) + " " + gameRooms.getPhase();
                break;
        }
        return string;
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
/*
        Bool firstVoiceInGameToBlockBecauseWeNeedToSleep = true

        День:
timer [00:02:00]
            chat only

        if (!f) {
            голос:
timer [00:01:00]
            chat + voice
            kill + view role
        } else f = !f

        ночь:
timer [00:02:00]
            mafiaChat + mafiaMove + docMove + mentMove + oneMoreEntity
            results [mafia kill moreChoice people or random if equal
                     don't kill if doc_choice
                     ladyOfNotHeartVremennyMove: can't choice in voice
                    ]



*/