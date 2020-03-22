package ru.eltex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.eltex.entity.GameRooms;
import ru.eltex.repos.RoomRepo;

import java.util.List;

/**
 * Класс-контроллер
 *
 * @author Evgesha
 * @version v1.0
 */

@Controller
public class WebController {

    @Autowired
    private RoomRepo roomRepo;

    @RequestMapping("/")
    public String signInView(Model model) {
        return "hello.html";
    }

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
