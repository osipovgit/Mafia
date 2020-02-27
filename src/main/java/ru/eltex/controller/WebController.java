package ru.eltex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.eltex.entity.User;
import ru.eltex.repos.UserRepo;

import java.util.Map;

/**
 * Класс-контроллер
 * @author Evgesha
 * @version v1.0
 */

@Controller
public class WebController {

    @RequestMapping("/mafia")
    public String index(Model model) {
        return "index1";
    }

    @RequestMapping("/playroom")
    public String playroom(Model model) {
        return "playroom.html";
    }

//    @RequestMapping("/hello")
//    public String hello(Model model) {
//        model.addAttribute("name",
//                new User(1, "Boris", 900));
//        return "hello";
//    }

    @RequestMapping("/bye")
    public String bye(Model model) {
//        model.addAttribute("name", new User(1, "Boris", 900));
        return "bye";
    }

}
