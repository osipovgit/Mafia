package ru.eltex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.eltex.entity.User;

/**
 * Класс-контроллер
 * @author Evgesha
 * @version v1.0
 */
@Controller
public class WebController {
    @RequestMapping("/")
    public String authorization(Model model) {
        return "authorization.html";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        return "signup.html";
    }

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
