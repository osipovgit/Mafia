package ru.eltex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.eltex.entity.User;
import ru.eltex.repos.RoomRepo;
import ru.eltex.repos.UserRepo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Класс-контроллер для отображения страниц.
 *
 * @author Evgesha
 * @version v1.0
 */
@Controller
public class WebController {
    /**
     * Поле подключения репозитория для взамимодействия пользвателя с БД.
     */
    @Autowired
    private UserRepo userRepo;
    /**
     * Поле подключения репозитория для взамимодействия комнат(-ы) с БД.
     */
    @Autowired
    private RoomRepo roomRepo;

    /**
     * Отображение первой страницы.
     *
     * @param model to view page
     * @return view hello.html
     */
    @RequestMapping("/")
    public String helloView(Model model) {
        return "hello.html";
    }

    /**
     * Отображение домашней страницы.
     * Так же осущствляет проверку на то, авторизировался ли пользователь:
     * - если да:  отображает домашнюю страницу [home.html];
     * - если нет: возвращает на страницу авторизации [redirect:/signup].
     *
     * @param model   to view page
     * @param request to get Cookies
     * @return view home.html or redirect:/signup
     */
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

    /**
     * Отображение страницы со списком комнат.
     * Так же осущствляет проверку на то, авторизировался ли пользователь:
     * - если да:  отображает страницу со списком комнат [playrooms.html];
     * - если нет: возвращает на страницу авторизации [redirect:/signup].
     *
     * @param model   to view page
     * @param request to get Cookies
     * @return view playrooms.html or redirect:/signup
     */
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
        User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
        userRepoById.setPassword(null);
        model.addAttribute("user", userRepoById);
        return "playrooms.html";
    }

    /**
     * Отображение игровой комнаты.
     * Так же осущствляет проверку на то, авторизировался ли пользователь, а также есть ли он в этой комнате:
     * - если да:  отображает страницу игровой комнаты [letsPlay.html];
     * - если нет: возвращает на страницу авторизации (пользователь не авторизован) [redirect:/signup]
     * или на страницу со списком комнат [redirect:/playrooms].
     *
     * @param model      to view page
     * @param roomNumber the room number
     * @param request    to get Cookies
     * @return view letsPlay.html redirect:/signup or redirect:/playrooms
     */
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
        if (roomRepo.findByNumberAndUserId(roomNumber, Long.parseLong(cookies[0].getValue())) != null) {
            User userRepoById = userRepo.findByUsernameOrId(null, Long.parseLong(cookies[0].getValue()));
            userRepoById.setPassword(null);
            model.addAttribute("user", userRepoById);
            return "letsPlay.html";
        } else return "redirect:/playrooms";
    }
}
