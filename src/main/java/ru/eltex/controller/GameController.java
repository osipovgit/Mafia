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
 * Класс - REST контроллер, основной класс, который отвечает за ход игры, создание, удаление комнат,
 * присоединения к ним пользователей, их взаимодействие в чате и отображение списка доступных комнат.
 * Обращение к методам происходит через ajax-запросы.
 *
 * @author Evgesha
 * @version v1.0
 */
@RestController
@RequestMapping("/playrooms")
public class GameController {
    /**
     * Поле номер комнаты.
     */
    private long roomNumber = 0;
    /**
     * Поле подключения репозитория для взамимодействия пользвателя с БД.
     */
    @Autowired
    private UserRepo userRepo;
    /**
     * Поле подключения репозитория для взамимодействия сообщений с БД.
     */
    @Autowired
    private MessagesRepo messagesRepo;
    /**
     * Поле подключения репозитория для отображения пользвателю информации о текущих комнатах из БД.
     */
    @Autowired
    private RoomsInfoRepo infoRepo;
    /**
     * Поле подключения репозитория для взамимодействия комнат(-ы) с БД.
     */
    @Autowired
    private RoomRepo roomRepo;

    /**
     * The String roles.
     */
    String[] stringRoles = {"mafia", "doctor", "civilian", "sheriff", "girl", "mafia", "civilian", "civilian", "mafia", "civilian"};

    /**
     * Метод для создания комнаты.
     * Устанавливает исходные значения для всех необходимых полей и сохраняет объект в БД.
     *
     * @param request to get Cookies [to find user by id]
     * @param model   the model
     * @return /playrooms/ + Room number
     */
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

    /**
     * Обновляет список доступных (находящихся на стадии регистрации) комнат.
     * Передает сформированный лист объектов через json пользователю.
     *
     * @param request to get Cookies [to find user by id]
     * @param model   the model
     * @return json (list rooms)
     */
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
                roomsInfo.setInfo(roomNumNow + " | Host: " + hostName + " | " + count + "/10");
                infoRepo.save(roomsInfo);
                count = 1;
                roomNumNow = gameRoom.getNumber();
                hostName = userRepo.findByUsernameOrId(null, gameRoom.getHostId()).getUsername();
            }
        }
        if (infoRepo.findByNumber(roomNumNow) == null & roomNumNow != -1) {
            RoomsInfo roomsInfo = new RoomsInfo();
            roomsInfo.setNumber(roomNumNow);
            roomsInfo.setInfo(roomNumNow + " | Host: " + hostName + " | " + count + "/10");
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

    /**
     * Метод для присоединения пользователя к комнате.
     * Устанавливает исходные значения для всех необходимых полей и сохраняет объект в БД.
     *
     * @param roomNumber the room number
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @return /playrooms/ + roomNumber
     */
    @GetMapping("/{roomNumber}/joinPlayer")
    public String joinPlayer(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        if (roomRepo.findTopByNumber(roomNumber) == null)
            return "This room has been deleted or the game has already started in it :(";
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
        if (roomRepo.findByNumberAndUserId(roomNumber, userRepoById.getId()) == null && gameRoomsList.size() < 10) {
            GameRooms gameRooms = new GameRooms();
            gameRooms.setUserId(userRepoById.getId());
            gameRooms.setHostId(roomRepo.findTopByNumber(roomNumber).getHostId());
            gameRooms.setNumber(roomNumber);
            gameRooms.setTimer(roomRepo.findTopByNumber(roomNumber).getTimer());
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

    /**
     * Метод удаления всех объектов комнат по номеру из БД, где пользователь является хостом
     * (в противном случае не работает [/playrooms/ + roomNumber]).
     * Удалить комнату возможно только в момент регистрации.
     * После начала игры эта возможность пропадает до следующего времени регистрации после окончания текущей игры.
     *
     * @param roomNumber the room number
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @return /playrooms or /playrooms/ + roomNumber
     */
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

    /**
     * Метод удаления объекта комнаты по номеру и id пользователя из БД (возможность покинуть данную комнату),
     * если пользователь действительно находится в этой комнате (в противном случае не работает [/playrooms/ + roomNumber]).
     * Покинуть комнату возможно только в момент регистрации.
     * После начала игры эта возможность пропадает до следующего времени регистрации после окончания текущей игры.
     *
     * @param roomNumber the room number
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @return /playrooms or /playrooms/ + roomNumber
     */
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

    /**
     * Метод для проверки условий запуска игры. После того, как набирается 5 и более человек, начинается отсчет времени
     * до начала игры (для ожидания большего количества игроков). В случае, если в комнате менее 5 игроков, идет стадия регистрации.
     * Игра начинается по прошествии минуты ожидания или если в комнате уже находится 10 человек (в таком случае игра начинается сразу).
     *
     * @param roomNumber the room number
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @return -3 - if room does not exist
     * 0  - if users less then 5
     * timer before begin - if 5 <= users < 10 and until a minute has passed
     * -2 - game is star
     */
    @GetMapping("/{roomNumber}/start")
    public Long startGame(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        GameRooms gameRoomTop = roomRepo.findTopByNumber(roomNumber);
        if (gameRoomTop == null)
            return -3L;
        List<GameRooms> gameRoomsList = roomRepo.findAllByNumber(roomNumber);
        Date dateNow = new Date();
        if (gameRoomsList.size() <= 2) { // TODO: change to < 5 & timer to 60 sec
            roomRepo.updateDate(roomNumber, dateNow.getTime());
            System.out.println("меньше мин");
            return 0L;
        } else if (dateNow.getTime() - gameRoomTop.getTimer() < 30000 & gameRoomsList.size() < 10 & gameRoomTop.getRole().equals("null")) {
            System.out.println((dateNow.getTime() - gameRoomTop.getTimer()) / 1000);
            return 60 - (dateNow.getTime() - gameRoomTop.getTimer()) / 1000;
        } else {
            System.out.println("старт");
            roomRepo.updatePhase(roomNumber, 1);
            return -2L;
        }
    }

    /**
     * Метод для отображения хосту комнаты кнопки, с помощью которой можно удалить комнату.
     * После начала игры эта кнопка пропадает до следующего времени регистрации после окончания текущей игры.
     *
     * @param roomNumber the room number
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @return 1 [user == host and game don't start] or 0
     */
    @GetMapping("/{roomNumber}/btnCheck")
    public Integer buttonDelRoomCheck(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        if (Long.parseLong(cookies[0].getValue()) == roomRepo.findTopByNumber(roomNumber).getHostId() & roomRepo.findTopByNumber(roomNumber).getRole().equals("null")) {
            return 1;
        } else return 0;
    }

    /**
     * Метод для обновления сообщений в чате (по номеру комнаты).
     * В течение стадии игры "Ночь" чат отображается только для пользователей с ролью "Мафия".
     *
     * @param roomNumber the room number
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @return json (list messages)
     */
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
        if (roomRepo.findTopByNumber(roomNumber).getPhase() < 3 | roomRepo.findTopByNumber(roomNumber).getPhase() == 4
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

    /**
     * Метод для добавления новых сообщений в чат (по номеру комнаты).
     * В течение стадии игры "Ночь" чат отображается только для пользователей с ролью "Мафия".
     * Для пользователей с ролью "Наблюдатель" ограничена возможность добавления новых сообщений до конца текущей партии.
     *
     * @param roomNumber the room number
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @param newMessage the new message
     * @return json (list messages)
     */
    @GetMapping("/{roomNumber}/chat/{newMessage}")
    public String updateChat(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model, @PathVariable String newMessage) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
        if (roomRepo.findByNumberAndUserId(roomNumber, userRepoById.getId()).getRole().equals("observer"))
            return "nope";
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

    /**
     * Метод для отображения списка пользователей в текущей комнате.
     *
     * @param roomNumber the room number
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @return json (list users in room)
     */
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

    /**
     * Метод для обозначения совершения действия с различными пользователями, входщими в состав комнаты, в определенную фазу игры:
     * - "День" - возвращает в поле воода строку в виде "<" + yourChoice + ">" для удобства общения;
     * - "Голосование" - устанавливает голос против выбранного игрока (кроме "Наблюдателя" и самого голосующего);
     * - "Ночь" - для ролей "Мафия", "Доктор", "Шериф", "Дама" ставит метки их уникальных действий в соответстветствующие поля в БД.
     * Голос за (против) игрока можно отдавать лишь один раз за одну фазу игры.
     * В соответствии с действием, чат дополняется соответственным сообщением с пометкой "sys:".
     *
     * @param roomNumber the room number
     * @param yourChoice (String) username
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @return "<" + yourChoice + ">" or ""
     */
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
                    if (!roomRepo.findByNumberAndUserId(roomNumber, userRepoById.getId()).getGirlChoice()) {
                        roomRepo.setVoiceOn(roomNumber,
                                roomRepo.findByNumberAndUserId(roomNumber, userRepo.findByUsernameOrId(yourChoice, null).getId()).getVote() + 1,
                                userRepo.findByUsernameOrId(yourChoice, null).getId());
                        Messages message = new Messages();
                        message.setRoomNumber(roomNumber);
                        message.setMessage("sys: " + userRepoById.getUsername() + ": votes for " + yourChoice);
                        messagesRepo.save(message);
                    } else return " <" + yourChoice + ">";
                    break;
                case 3:
                    switch (gameRooms.getRole()) {
                        case "mafia":
                            roomRepo.setMafiaChoiceOn(roomNumber, roomRepo.findByNumberAndUserId(roomNumber, userRepo.findByUsernameOrId(yourChoice, null).getId()).getMafiaChoice() + 1, userRepo.findByUsernameOrId(yourChoice, null).getId());
                            Messages message = new Messages();
                            message.setRoomNumber(roomNumber);
                            message.setMessage("sys: " + userRepoById.getUsername() + ": votes for " + yourChoice);
                            messagesRepo.save(message);
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

    /**
     * Метод, реализующий основной цикл игры. Имеет 4 фазы (0 - подведение итогов, 1 - "День", 2 - "Голосование" и 3 - "Ночь").
     * В самом начале в случайном порядке устанавливаются роли, в соответствии с количеством игроков.
     * Фазы 1-3 ограничены временем в соответствии с выбранными нами оптимальными правилами игры.
     * Фаза 1: Время для общения! В течении полутора минут игроки ведут обсуждения в чате и ожидают голосования, чтобы выбрать жертву.
     * Фаза 2: Время голосования! В течении 40 секунд игроки отдают свои голоса против соперников.
     * При подведении итогов "Выбывает" игрок, набравший наибольшее число голосов или случайный игрок, в случае если голоса равны.
     * Если "Ночью" игрок был выбран "Дамой", он не имеет права голосовать.
     * Фаза 3: Время ночи! В течении минуты в чате "Мафия" выбирает жертву и голосует за нее.
     * При подведении итогов "Выбывает" игрок, набравший наибольшее число голосов или случайный игрок, в случае если голоса равны,
     * если на него не пал выбор "Доктора", который спасает бедолагу от "Смерти".
     * "Шериф" при выборе игрока узнает, является ли он мафией.
     * "Дама" выбирает игрока, который в течении следующего голосования околдован ее невероятными чарами настолько, что не может проронить ни слова.
     * "Доктор" выбирает игрока, который бует спасен от "Смерти", если на него пал выбор мафии.
     * Фаза 0: Подведение итогов игры: по итогам одной из фаз игры количество "Мафий" или "Мирных жителей" стало равно 0.
     * В данной фазе в чат выводится сообщение о победе той или иной команды,
     * параметры игроков устанавливаются в изначальные значения, снова начинается фаза регистрации и ожидания новых игроков.
     *
     * @param roomNumber the room number
     * @param request    to get Cookies [to find user by id]
     * @param model      the model
     * @return json (string to view) or ""
     */
    @GetMapping("/{roomNumber}/game")
    public String gameMode(@PathVariable("roomNumber") Long roomNumber, HttpServletRequest request, Model model) {
        GameRooms gameRooms = roomRepo.findTopByNumber(roomNumber);
        if (gameRooms.getRole().equals("null")) {
            roomRepo.updatePhase(roomNumber, 4);
            if (gameRooms.getPhase() == 0 | gameRooms.getPhase() == 4)
                return "";
            roomRepo.updateDate(roomNumber, new Date().getTime());
            List<GameRooms> gameRoomsList = roomRepo.findAllByNumber(roomNumber);
            Collections.shuffle(gameRoomsList);
            int indexRole = 0;
            System.out.println("Set roles: ");
            for (GameRooms gameRoom : gameRoomsList) {
                roomRepo.setRoles(roomNumber, stringRoles[indexRole++], gameRoom.getUserId());
                System.out.println(gameRoom.getUserId() + " " + gameRoom.getRole());
            }
            roomRepo.updatePhase(roomNumber, 1);
            Messages messages = new Messages();
            messages.setRoomNumber(roomNumber);
            messages.setMessage("Day is coming...");
            messagesRepo.save(messages);
        }
        Date dateNow = new Date();
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
        String string = "";
        string += "[{\"role\":\"" + roomRepo.findByNumberAndUserId(roomNumber, userRepoById.getId()).getRole().toUpperCase() + "\",\"phase\":" + gameRooms.getPhase();
        switch (gameRooms.getPhase()) {
            case 0:
                roomRepo.updatePhase(roomNumber, 4);
                List<GameRooms> roomsOrderD = roomRepo.findAllByNumberOrderByVoteDesc(roomNumber);
                int countMaf = 0;
                messagesRepo.deleteAllByRoomNumber(roomNumber);
                for (GameRooms gameRoom : roomsOrderD) {
                    countMaf += gameRoom.getRole().equals("mafia") ? 1 : 0;
                    System.out.println(gameRoom);
                }
                Messages message1 = new Messages();
                message1.setRoomNumber(roomNumber);
                System.out.println("CountMafia final:" + countMaf);
                if (countMaf == 0) {
                    message1.setMessage("Civilians won!!!");
                } else
                    message1.setMessage("Mafia won!!!");
                messagesRepo.save(message1);
                roomRepo.setVoteZero(roomNumber, 0);
                roomRepo.updateStage(roomNumber, true);
                roomRepo.updateDate(roomNumber, new Date().getTime());
                roomRepo.resetAll(roomNumber, false, false, 0);
                roomRepo.resetGirlChoice(roomNumber, false);
                roomRepo.resetRoles(roomNumber, "null");
                return "";
            case 1:
                if (dateNow.getTime() - gameRooms.getTimer() >= 90000) {
                    roomRepo.updatePhase(roomNumber, 4);
                    System.out.println("Phase 1");
                    if (gameRooms.getStageOne()) {
                        roomRepo.updatePhase(roomNumber, 3);
                        roomRepo.updateStage(roomNumber, false);
                    } else {
                        roomRepo.updatePhase(roomNumber, 2);
                        Messages messages = new Messages();
                        messages.setRoomNumber(roomNumber);
                        messages.setMessage("Voting begins...");
                        messagesRepo.save(messages);
                    }
                    roomRepo.updateDate(roomNumber, new Date().getTime());
                    roomRepo.setDoneMoveFalse(roomNumber, false);
                } else {
                    List<GameRooms> roomsOrderNight = roomRepo.findAllByNumberOrderByMafiaChoiceDesc(roomNumber);
                    roomsOrderNight.removeIf(x -> x.getRole().equals("observer"));
                    int countWithMafia = roomsOrderNight.size(), countOther;
                    roomsOrderNight.removeIf(x -> x.getRole().equals("mafia"));
                    countOther = roomsOrderNight.size();
                    string += ",\"timer\":\"Наступил день  |  0" + (((dateNow.getTime() - gameRooms.getTimer()) / 1000) < 30 ? "1:" : "0:") + (((dateNow.getTime() - gameRooms.getTimer()) / 1000) >= 30 ? ((((dateNow.getTime() - gameRooms.getTimer()) / 1000 - 30) > 50 ? "0" : "") + (60 - ((dateNow.getTime() - gameRooms.getTimer()) / 1000 - 30) % 60)) : ((((dateNow.getTime() - gameRooms.getTimer()) / 1000) > 20 ? "0" : "") + (30 - ((dateNow.getTime() - gameRooms.getTimer()) / 1000)))) + " | mafia: " + (countWithMafia - countOther) + " | civilian: " + countOther + "\"";
                }
                break;
            case 2:
                if (dateNow.getTime() - gameRooms.getTimer() >= 40000) {
                    roomRepo.updatePhase(roomNumber, 4);
                    System.out.println("Phase 2");
                    List<GameRooms> roomsOrderDesc = roomRepo.findAllByNumberOrderByVoteDesc(roomNumber);
                    int countMafia = 0, countOther = 0, maxVote = 0, countVote = 0;
                    roomsOrderDesc.removeIf(x -> x.getRole().equals("observer"));
                    for (GameRooms gameRoom : roomsOrderDesc) {
                        maxVote = Math.max(gameRoom.getVote(), maxVote);
                        countVote = gameRoom.getVote() == maxVote ? ++countVote : countVote;
                        countMafia += gameRoom.getRole().equals("mafia") ? 1 : 0;
                        countOther += !gameRoom.getRole().equals("mafia") ? 1 : 0;
                    }
                    System.out.println("mV:" + maxVote + " couV:" + countVote + " cM:" + countMafia + " cO:" + countOther);
                    countVote = (int) (random() * countVote);
                    for (GameRooms gameRoom : roomsOrderDesc) {
                        System.out.println("cV: " + countVote + " user: " + gameRoom.getUserId() + " role: " + gameRoom.getRole());
                        if (countVote-- == 0) {
                            roomRepo.setRoles(roomNumber, "observer", gameRoom.getUserId());
                            if (gameRoom.getRole().equals("mafia"))
                                countMafia -= 1;
                            else countOther -= 1;
                            break;
                        }
                    }
                    if (countMafia == 0 | countOther == 0) {
                        roomRepo.updatePhase(roomNumber, 0);
                    } else {
                        roomRepo.updatePhase(roomNumber, 3);
                        roomRepo.updateDate(roomNumber, new Date().getTime());
                        roomRepo.setDoneMoveFalse(roomNumber, false);
                        roomRepo.setVoteZero(roomNumber, 0);
                        roomRepo.resetGirlChoice(roomNumber, false);
                        messagesRepo.deleteAllByRoomNumber(roomNumber);
                    }
                } else {
                    List<GameRooms> roomsOrderNight = roomRepo.findAllByNumberOrderByMafiaChoiceDesc(roomNumber);
                    roomsOrderNight.removeIf(x -> x.getRole().equals("observer"));
                    int countWithMafia = roomsOrderNight.size(), countOther;
                    roomsOrderNight.removeIf(x -> x.getRole().equals("mafia"));
                    countOther = roomsOrderNight.size();
                    string += ",\"timer\":\"Время голосования  |  00:" + (((dateNow.getTime() - gameRooms.getTimer()) / 1000) > 30 ? "0" : "") + (40 - ((dateNow.getTime() - gameRooms.getTimer()) / 1000)) + " | mafia: " + (countWithMafia - countOther) + " | civilian: " + countOther + "\"";
                }
                break;
            case 3:
                if (dateNow.getTime() - gameRooms.getTimer() >= 60000) {
                    roomRepo.updatePhase(roomNumber, 4);
                    Messages mesNight = new Messages();
                    mesNight.setRoomNumber(roomNumber);
                    mesNight.setMessage("Night is coming... Only MAFIA in chat!");
                    messagesRepo.save(mesNight);
                    messagesRepo.deleteAllByRoomNumber(roomNumber);
                    Messages messages = new Messages();
                    messages.setRoomNumber(roomNumber);
                    System.out.println("Phase 3");
                    List<GameRooms> roomsOrderNight = roomRepo.findAllByNumberOrderByMafiaChoiceDesc(roomNumber);
                    int maxVote = 0, countVote = 0;
                    roomsOrderNight.removeIf(x -> x.getRole().equals("observer"));
                    int countWithMafia = roomsOrderNight.size();
                    roomsOrderNight.removeIf(x -> x.getRole().equals("mafia"));
                    for (GameRooms gameRoom : roomsOrderNight) {
                        System.out.println(gameRoom);
                        maxVote = Math.max(gameRoom.getMafiaChoice(), maxVote);
                        countVote = gameRoom.getMafiaChoice() == maxVote ? ++countVote : countVote;
                    }
                    System.out.println("mV:" + maxVote + " couV:" + countVote);
                    countVote = (int) (random() * countVote);
                    int obs = 0;
                    for (GameRooms gameRoom : roomsOrderNight) {
                        System.out.println("cV: " + countVote + " user: " + gameRoom.getUserId() + " role: " + gameRoom.getRole());
                        if (countVote-- == 0) {
                            if (!gameRoom.getDocChoice()) {
                                roomRepo.setRoles(roomNumber, "observer", gameRoom.getUserId());
                                messages.setMessage("Mafia kill " + userRepo.findByUsernameOrId(null, gameRoom.getUserId()).getUsername() + " :(");
                                ++obs;
                            } else messages.setMessage("Doctor save, keep calm :)");
                        }
                    }
                    messagesRepo.save(messages);
                    if (roomsOrderNight.size() - obs == 0) {
                        roomRepo.updatePhase(roomNumber, 0);
                    } else {
                        roomRepo.updatePhase(roomNumber, 1);
                        roomRepo.updateDate(roomNumber, new Date().getTime());
                        roomRepo.resetAll(roomNumber, false, false, 0);
                        Messages messages1 = new Messages();
                        messages1.setRoomNumber(roomNumber);
                        messages1.setMessage("Day is coming...");
                        messagesRepo.save(messages1);
                    }
                } else {
                    List<GameRooms> roomsOrderNight = roomRepo.findAllByNumberOrderByMafiaChoiceDesc(roomNumber);
                    roomsOrderNight.removeIf(x -> x.getRole().equals("observer"));
                    int countWithMafia = roomsOrderNight.size(), countOther;
                    roomsOrderNight.removeIf(x -> x.getRole().equals("mafia"));
                    countOther = roomsOrderNight.size();
                    string += ",\"timer\":\"Наступила ночь  |  00:" + (((dateNow.getTime() - gameRooms.getTimer()) / 1000) > 50 ? "0" : "") + (60 - ((dateNow.getTime() - gameRooms.getTimer()) / 1000)) + " | mafia: " + (countWithMafia - countOther) + " | civilian: " + countOther + "\"";
                }
                break;
            case 4:
                break;
        }
//        System.out.println(string + "}]");
        return string + "}]";
    }
}