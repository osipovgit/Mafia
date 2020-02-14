package ru.eltex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @RequestMapping("/mafia")
    public String index(Model model) {
        return "index1";
    }

    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("name",
                new User(1, "Boris", 900));
        return "hello";
    }

    @RequestMapping("/bye")
    public String bye(Model model) {
        model.addAttribute("name", new User(1, "Boris", 900));
        return "bye";
    }

}
