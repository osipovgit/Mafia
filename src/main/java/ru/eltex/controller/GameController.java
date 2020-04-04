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
import ru.eltex.entity.Messages;
import ru.eltex.entity.RoomsInfo;
import ru.eltex.entity.User;
import ru.eltex.repos.MessagesRepo;
import ru.eltex.repos.RoomRepo;
import ru.eltex.repos.RoomsInfoRepo;
import ru.eltex.repos.UserRepo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.random;

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

    @Autowired
    private MessagesRepo messagesRepo;

    @Autowired
    private RoomsInfoRepo infoRepo;

    private final RoomRepo roomRepo;

    public GameController(RoomRepo roomRepo) {
        this.roomRepo = roomRepo;
    }

    ArrayList stringList = new ArrayList(Arrays.asList("mafia", "doctor", "civilian", "sheriff", "girl", "civilian", "civilian", "mafia", "mafia", "civilian"));

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
        gameRooms.setDone_move(false);
        gameRooms.setGirlChoice(false);
        gameRooms.setDocChoice(false);
        gameRooms.setMafiaChoice(0);
        gameRooms.setVote(0);
        gameRooms.setRole("null");
        roomRepo.save(gameRooms);
        System.out.println("Создана комната: " + gameRooms.getNumber());
        return "/playrooms/" + gameRooms.getNumber();
    }

    @GetMapping("/update")
    public String viewAllRooms(HttpServletRequest request, Model model) {
        if (roomRepo.findAll() == null)
            return "";
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        List<GameRooms> gameRooms = roomRepo.findAll().stream().sorted(Comparator.comparing(GameRooms::getNumber)).collect(Collectors.toList());
        int count = 0;
        String hostName = "";
        long roomNumNow = -1;
        infoRepo.deleteAll();
        for (GameRooms gameRoom : gameRooms) {
            if (!gameRoom.getRole().equals("null") & roomRepo.findByNumberAndUserId(gameRoom.getNumber(), Long.parseLong(cookies[0].getValue())) == null)
                continue;
            if (roomNumNow == -1) {
                roomNumNow = gameRoom.getNumber();
            }
            if (gameRoom.getNumber() == roomNumNow) {
                ++count;
                hostName = userRepo.findByUsernameOrId(null, gameRoom.getHostId()).getUsername();
            } else {
                RoomsInfo roomsInfo = new RoomsInfo();
                roomsInfo.setNumber(roomNumNow);
                roomsInfo.setInfo(roomNumNow + "       |        Host: " + hostName + "       |       " + count + "/10");
                infoRepo.save(roomsInfo);
                count = 1;
                roomNumNow = gameRoom.getNumber();
            }
        }
        if (infoRepo.findByNumber(roomNumNow) == null & roomNumNow != -1) {
            RoomsInfo roomsInfo = new RoomsInfo();
            roomsInfo.setNumber(roomNumNow);
            roomsInfo.setInfo(roomNumNow + "       |        Host: " + hostName + "       |       " + count + "/10");
            infoRepo.save(roomsInfo);
        }
        List<RoomsInfo> roomsInfos = infoRepo.findAll();
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(roomsInfos);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        System.out.println(jsonStr);
        return jsonStr;
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
        if (roomRepo.findByNumberAndUserId(roomNumber, userRepoById.getId()) != null)
            return "/playrooms/" + roomNumber;
        if (roomRepo.findByNumberAndUserId(roomNumber, userRepoById.getId()) == null && gameRoomsList.size() < 10) {       //TODO: Think about condition
            GameRooms gameRooms = new GameRooms();
            gameRooms.setUserId(userRepoById.getId());
            gameRooms.setHostId(roomRepo.findTopByNumber(roomNumber).getHostId());
            gameRooms.setNumber(roomNumber);
            Date date = new Date();
            gameRooms.setTimer(date.getTime());
            gameRooms.setStageOne(true);
            gameRooms.setPhase(1);
            gameRooms.setDone_move(false);
            gameRooms.setGirlChoice(false);
            gameRooms.setDocChoice(false);
            gameRooms.setMafiaChoice(0);
            gameRooms.setVote(0);
            gameRooms.setRole("null");
            roomRepo.save(gameRooms);
            System.out.println("Пользователь: " + userRepoById.getUsername() + " присоединяется к комнате: " + roomNumber);
            return "/playrooms/" + roomNumber;
        } else return "/playrooms";
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
        if (userRepoById.getId().equals(gameRooms.getHostId()) && gameRooms.getRole().equals("null")) {
            messagesRepo.deleteAllByRoomNumber(roomNumber);
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
        if (userRepoById.getId().equals(gameRooms.getUserId()) && gameRooms.getRole().equals("null")) {
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
        if (gameRoomTop == null)
            return -3L;
        if (gameRoomsList.size() <= 1) { // TODO: change to < 5
            roomRepo.updateDate(roomNumber, dateNow.getTime());
            return 0L;
        } else if (dateNow.getTime() - gameRoomTop.getTimer() < 15000 & gameRoomsList.size() < 10 & gameRoomTop.getRole().equals("null")) {
            return (dateNow.getTime() - gameRoomTop.getTimer()) / 1000;
        } else {
            if (gameRoomTop.getRole().equals("null"))
                roomRepo.updateDate(roomNumber, dateNow.getTime());
            return -2L;
        }
    }

    @GetMapping("/{roomNumber}/btnCheck")
    public Integer buttonDelRoomCheck(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        if (Long.parseLong(cookies[0].getValue()) == roomRepo.findTopByNumber(roomNumber).getHostId()) {
            return 1;
        } else return 0;
    }

    @GetMapping("/{roomNumber}/chat")
    public String updateChatPerSec(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        if (roomRepo.findTopByNumber(roomNumber) == null) {
            return "";
        }
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
        if (roomRepo.findTopByNumber(roomNumber).getPhase() < 3
                | (roomRepo.findTopByNumber(roomNumber).getPhase() == 3 & roomRepo.findByNumberAndUserId(roomNumber, userRepoById.getId()).getRole().equals("mafia"))) {
            List<Messages> messages = messagesRepo.findAllByRoomNumberOrderById(roomNumber);
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = null;
            try {
                jsonStr = mapper.writeValueAsString(messages);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
//        System.out.println(jsonStr);
            return jsonStr;
        } else return "";
    }

    @GetMapping("/{roomNumber}/chat/{newMessage}")
    public String updateChat(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model, @PathVariable String newMessage) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
        if (roomRepo.findTopByNumber(roomNumber).getPhase() < 3 | (roomRepo.findTopByNumber(roomNumber).getPhase() == 3 & roomRepo.findByNumberAndUserId(roomNumber, userRepoById.getId()).getRole().equals("mafia"))) {
            if (!newMessage.equals("")) {
                Messages messages = new Messages();
                messages.setRoomNumber(roomNumber);
                messages.setMessage(userRepoById.getUsername() + ": " + newMessage);
                messagesRepo.save(messages);
            }
            List<Messages> messages = messagesRepo.findAllByRoomNumberOrderById(roomNumber);
            ObjectMapper mapper = new ObjectMapper();
            String jsonStr = null;
            try {
                jsonStr = mapper.writeValueAsString(messages);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
//        System.out.println(jsonStr);
            return jsonStr;
        } else return "";
    }

    @GetMapping("/{roomNumber}/update_view_players")
    public String playersInRoom(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        List<GameRooms> rooms = roomRepo.findAllByNumber(roomNumber);
        List<User> users = new ArrayList<>();
        for (GameRooms room : rooms) {
            User user = userRepo.findByUsernameOrId(null, room.getUserId());
            user.setPassword(null);
            users.add(user);
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = null;
        try {
            jsonStr = mapper.writeValueAsString(users);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
//        System.out.println(jsonStr);
        return jsonStr;
    }

    @GetMapping("/{roomNumber}/click_to_user/{yourChoice}")
    public String iLikeToMoveIt(@PathVariable("roomNumber") Long roomNumber, @PathVariable("yourChoice") String yourChoice, HttpServletRequest request, Model model) {
        GameRooms gameRoomsTop = roomRepo.findTopByNumber(roomNumber);
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
        GameRooms gameRooms = roomRepo.findByNumberAndUserId(roomNumber, userRepoById.getId());
        if (!userRepoById.getUsername().equals(yourChoice) && !gameRooms.getRole().equals("observer")
                && gameRooms != null && !gameRooms.getDone_move() && !roomRepo.findByNumberAndUserId(roomNumber, userRepo.findByUsernameOrId(yourChoice, null).getId()).getRole().equals("observer")) {
            switch (gameRoomsTop.getPhase()) {
                case 1:
                    return "<" + yourChoice + ">";
                case 2:
                    if (!roomRepo.findByNumberAndUserId(roomNumber, userRepo.findByUsernameOrId(yourChoice, null).getId()).getGirlChoice()) {
                        roomRepo.setVoiceOn(roomNumber,
                                roomRepo.findByNumberAndUserId(roomNumber, userRepo.findByUsernameOrId(yourChoice, null).getId()).getVote() + 1,
                                userRepo.findByUsernameOrId(yourChoice, null).getId());
                        Messages message = new Messages();
                        message.setRoomNumber(roomNumber);
                        message.setMessage("sys: " + userRepoById.getUsername() + ": votes for " + yourChoice);
                        messagesRepo.save(message);
                    } else return "<" + yourChoice + ">";
                    break;
                case 3:
                    switch (gameRooms.getRole()) {
                        case "mafia":
                            roomRepo.setMafiaChoiceOn(roomNumber, roomRepo.findByNumberAndUserId(roomNumber, userRepo.findByUsernameOrId(yourChoice, null).getId()).getMafiaChoice() + 1, userRepo.findByUsernameOrId(yourChoice, null).getId());
                            break;
                        case "doctor":
                            roomRepo.setDoctorChoiceOn(roomNumber, true, userRepo.findByUsernameOrId(yourChoice, null).getId());
                            break;
                        case "sheriff":
                            return (roomRepo.findByNumberAndUserId(roomNumber, userRepo.findByUsernameOrId(yourChoice, null).getId()).getRole().equals("mafia") ? "You found the mafia!" : "You have not found the mafia!");
                        case "girl":
                            roomRepo.setGirlChoiceOn(roomNumber, true, userRepo.findByUsernameOrId(yourChoice, null).getId());
                            break;
                    }
                    break;
            }
            roomRepo.setDoneMoveReverse(roomNumber, true, userRepoById.getId());
        }
        return "";
    }

    @GetMapping("/{roomNumber}/game")
    public String gameMode(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        GameRooms gameRooms = roomRepo.findTopByNumber(roomNumber);
        if (gameRooms.getRole().equals("null")) {
            List<GameRooms> gameRoomsList = roomRepo.findAllByNumber(roomNumber);
            Collections.shuffle(gameRoomsList);
            int indexRole = 0;
            for (GameRooms gameRoom : gameRoomsList) {
                roomRepo.setRoles(roomNumber, (String) stringList.get(indexRole++), gameRoom.getUserId());
            }
        }
        Date dateNow = new Date();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
        String string = "[{";
        string += "\"role\":\"" + roomRepo.findByNumberAndUserId(roomNumber, userRepoById.getId()).getRole().toUpperCase() + "\",\"phase\":" + gameRooms.getPhase() + ",";
        switch (gameRooms.getPhase()) {
            case 0:
                List<GameRooms> roomsOrderD = roomRepo.findAllByNumberOrderByVoteDesc(roomNumber);
                int countMaf = 0;
                for (GameRooms gameRoom : roomsOrderD) {
                    countMaf += gameRoom.getRole().equals("mafia") ? 1 : 0;
                }
                if (countMaf == 0)
                    string = "Civilians won!!!";
                else string = "Mafia won!!!";
                messagesRepo.deleteAllByRoomNumber(roomNumber);
                roomRepo.updatePhase(roomNumber, 1);
                roomRepo.updateStage(roomNumber, true);
                roomRepo.updateDate(roomNumber, new Date().getTime());
                roomRepo.resetAll(roomNumber, false, false, false, 0);
                roomRepo.resetRoles(roomNumber, "null");
                return string;
            case 1:
                if (dateNow.getTime() - gameRooms.getTimer() >= 12000) {
                    if (gameRooms.getStageOne()) {
                        roomRepo.updatePhase(roomNumber, 3);
                        roomRepo.updateStage(roomNumber, false);
                    } else
                        roomRepo.updatePhase(roomNumber, 2);
                    roomRepo.updateDate(roomNumber, new Date().getTime());
                    roomRepo.setDoneMoveFalse(roomNumber, false);
                } else
                    string += "\"timer\":\"0" + (1 - ((dateNow.getTime() - gameRooms.getTimer()) / 60000)) + ":" + ((((dateNow.getTime() - gameRooms.getTimer()) / 1000) % 60) > 50 ? "0" : "") + (60 - (((dateNow.getTime() - gameRooms.getTimer()) / 1000)) % 60) + "\"";
                break;
            case 2:
                if (dateNow.getTime() - gameRooms.getTimer() >= 6000) {
                    List<GameRooms> roomsOrderDesc = roomRepo.findAllByNumberOrderByVoteDesc(roomNumber);
                    int countMafia = 0, countOther = 0, maxVote = 0, countVote = 0;
                    for (GameRooms gameRoom : roomsOrderDesc) {
                        maxVote = Math.max(gameRoom.getVote(), maxVote);
                        countVote = gameRoom.getVote() == maxVote ? ++countVote : countVote;
                        countMafia += gameRoom.getRole().equals("mafia") ? 1 : 0;
                        countOther += !gameRoom.getRole().equals("mafia") & !gameRoom.getRole().equals("observer") ? 1 : 0;
                        if (gameRoom.getRole().equals("observer"))
                            roomsOrderDesc.remove(gameRoom);
                    }
                    countVote = (int) (random() * countVote);
                    for (GameRooms gameRoom : roomsOrderDesc) {
                        if (countVote-- == 0) {
                            roomRepo.setRoles(roomNumber, "observer", gameRoom.getUserId());
                            if (gameRoom.getRole().equals("mafia"))
                                --countMafia;
                            else --countOther;
                            break;
                        }
                    }
                    if (countMafia == 0 | countOther == 0)
                        roomRepo.updatePhase(roomNumber, 0);
                    else
                        roomRepo.updatePhase(roomNumber, 3);
                    roomRepo.updateDate(roomNumber, new Date().getTime());
                    roomRepo.setDoneMoveFalse(roomNumber, false);
                    roomRepo.setVoteZero(roomNumber, 0);
                    messagesRepo.deleteAllByRoomNumber(roomNumber);
                }
                string += "\"timer\":\"00:" + (((dateNow.getTime() - gameRooms.getTimer()) / 1000) > 50 ? "0" : "") + (60 - ((dateNow.getTime() - gameRooms.getTimer()) / 1000)) + "\"";
                break;
            case 3:
                if (dateNow.getTime() - gameRooms.getTimer() >= 12000) {
                    messagesRepo.deleteAllByRoomNumber(roomNumber);
                    roomRepo.updatePhase(roomNumber, 1);
                    Date date = new Date();
                    roomRepo.updateDate(roomNumber, date.getTime());
                    roomRepo.resetAll(roomNumber, false, false, false, 0);
                }
                string += "\"timer\":\"0" + (1 - ((dateNow.getTime() - gameRooms.getTimer()) / 60000)) + ":" + ((((dateNow.getTime() - gameRooms.getTimer()) / 1000) % 60) > 50 ? "0" : "") + (60 - (((dateNow.getTime() - gameRooms.getTimer()) / 1000)) % 60) + "\"";
                break;
        }
//        System.out.println(string + "}]");
        return string + "}]";
    }
}
/*
{
    "[{"timer":" + 1 + ","phase":" + "f","role":"f","infoRole":"f"}]

    ["numberRoom": "roomNum"
    "messages": [{"message" : .getUsername() + ": " + "привет дарагие патпищики"},
                 {"message" : "Lanturn: @$%!* иди"},
                 {"message" : "Boris: у вас осталось шесть дней"},
                 {"message" : "Lanturn: *!%$@ идем мы походу (ред.)"},
                 {"message" : "*Биба и Боба покидают чат*"}
                ]]
}
i: hiBorya:byeMax:sent code
-------------------------     ------
|  hi                    |   | sent |
-------------------------     ------
 */
/*
        Bool firstVoiceInGameToBlockBecauseWeNeedToSleep = true

        День:
timer [00:02:00]
            chat only [username]

        if (!f) {
            голос:
timer [00:01:00]
            voice + chat
            kill + view role
        } else f = !f

        ночь:
timer [00:02:00]
            mafiaChat + mafiaMove + docMove + mentMove + oneMoreEntity
            results [mafia kill moreChoice people or random if equal
                     don't kill if doc_choice
                     ladyOfNotHeartVremennyMove: can't choice in voice
                    ]


, function () {
                for (let i = 0; i < 24; i++) {
                    if (key === "userId") {
                        viewewew.className = 'button';
                        viewewew.innerHTML = value;
                        // var text = document.createTextNode("SOMETHING");
                        $("#usersList").html(viewewew);
                        // viewewew.appendChild(text);
                        // viewewew.innerHTML = "<br>";
                    }
                }
            }
*/