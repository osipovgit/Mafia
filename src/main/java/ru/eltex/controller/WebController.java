package ru.eltex.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.eltex.entity.GameRooms;
import ru.eltex.entity.User;
import ru.eltex.repos.RoomRepo;
import ru.eltex.repos.UserRepo;

import java.util.List;
import java.util.Optional;

/**
 * Класс-контроллер
 *
 * @author Evgesha
 * @version v1.0
 */

@Controller
public class WebController {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoomRepo roomRepo;

    @RequestMapping("/")
    public String signInView(Model model) {
        return "hello.html";
    }

    @RequestMapping("/home/id{id}")
    public String homePageView(Model model, @PathVariable("id") Long id) {
        User userRepoById = userRepo.findByUsernameOrId(null, id);
        userRepoById.setReady(false);
        userRepoById.setPassword(null);
//        ObjectMapper mapper = new ObjectMapper();
//        String str = mapper.writeValueAsString(userRepoById);
//        System.out.println(str);
        model.addAttribute("user", userRepoById);
        return "home.html";
    }

    //    TODO: Фикс возможность зайти под любого пользователя (мб куки), как сохранить где-то под кого зашли? потом еще и комнаты надо сверять
    @RequestMapping("/playrooms")
    public String playRoomsView(Model model) {
        return "playrooms.html";
    }

    @RequestMapping("/playrooms/{roomNumber}")
    public String roomId(Model model, @PathVariable long roomNumber) {
        return "letsPlay.html";
    }

    @GetMapping("/playrooms")
    public List<GameRooms> playroom(Model model) {

        return roomRepo.findAll();
    }

//    @RequestMapping("/hello")
//    public String hello(Model model) {
//        model.addAttribute("name",
//                new User(1, "Boris", 900));
//        return "hello";
//    }

}
