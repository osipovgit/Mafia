package ru.eltex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.eltex.entity.GameRooms;
import ru.eltex.entity.User;
import ru.eltex.repos.RoomRepo;
import ru.eltex.repos.UserRepo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
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
    private UserRepo userRepo;

    @Autowired
    private RoomRepo roomRepo;

    @RequestMapping("/")
    public String signInView(Model model) {
        return "hello.html";
    }

    @RequestMapping("/home")
    public String homePageView(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies)
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        if (request.getCookies() == null)
            return "redirect:/signup";
        else if (!cookies[0].getName().equals("userId"))
            return "redirect:/signup";
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
        userRepoById.setPassword(null);
        model.addAttribute("user", userRepoById);
        return "home.html";
    }

    @RequestMapping("/playrooms/{roomNumber}")
    public String roomId(Model model, @PathVariable(value = "roomNumber") Long roomNumber, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        }
        if (request.getCookies() == null)
            return "redirect:/signup";
        else if (!cookies[0].getName().equals("userId"))
            return "redirect:/signup";
        if (roomRepo.findByNumberAndUserId(roomNumber, Long.parseLong(cookies[0].getValue())) != null)
            return "letsPlay.html";
        else return "redirect:/playrooms";
    }

    @GetMapping("/playrooms")
    public String playroom(Model model, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userId")) {
                cookies[0] = cookie;
                break;
            }
        }
        if (request.getCookies() == null)
            return "redirect:/signup";
        else if (!cookies[0].getName().equals("userId"))
            return "redirect:/signup";
        else return "playrooms.html";
    }
}
