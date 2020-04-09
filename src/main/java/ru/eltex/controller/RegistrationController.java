package ru.eltex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.eltex.entity.User;
import ru.eltex.repos.UserRepo;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Класс-контроллер для регистрации.
 */
@Controller
public class RegistrationController {
    /**
     * Поле подключения PasswordEncoder, для храниения и сравнения паролей в неявном виде (кодировка BCrypt).
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepo userRepo;

    /**
     * Альтернативное (относительно @Autowired) объявление поля подключения репозитория для взамимодействия пользвателя с БД.
     *
     * @param userRepo the user repo
     */
    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Авторизация пользователя в нашей системе. Выполняет поиск пользователя в БД, проверяет, совпадают ли логин и пароль:
     * - если да:  добавляет cookie с именем пользователя и его идентификатором переходит на домашнюю страницу [/home];
     * - если нет: возвращает на страницу авторизации [/authorization.html].
     *
     * @param user     it receives data from forms
     * @param model    to view page
     * @param response to add Cookie
     * @return /authorization.html or redirect:/home
     */
    @PostMapping("/")
    public String signIn(User user, Model model, HttpServletResponse response) {
        User userRepoByUsername = userRepo.findByUsernameOrId(user.getUsername(), null);
        if (userRepoByUsername == null || !passwordEncoder.matches(user.getPassword(), userRepoByUsername.getPassword())) {
            return "/authorization.html";
        }
        Cookie cookie = new Cookie("userId", userRepoByUsername.getId().toString());
        response.addCookie(cookie);
        return "redirect:/home";
    }

    /**
     * Отображение страницы регистрации.
     *
     * @param model to view page
     * @return view signup.html
     */
    @GetMapping("/signup")
    public String signUp(Model model) {
        return "signup.html";
    }

    /**
     * Регистрация пользователя в нашей системе. Выполняет поиск пользователя по логину в БД, проверяет, есть ли уже такое:
     * - если да:  добавляет пользователя в БД и переходит на страницу авторизации [/authorization];
     * - если нет: возвращает на страницу регистрации [signup.html].
     *
     * @param user  it receives data from forms
     * @param model to view page
     * @return view signup.html or redirect:/authorization
     */
    @PostMapping("/signup")
    public String signUpNewUser(User user, Model model) {
        User userFromDb = userRepo.findByUsernameOrId(user.getUsername(), null);
        if (userFromDb != null) {
            return "signup.html";
        }
        user.setCountGame(0L);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setGameMode(0);
        userRepo.save(user);
        return "redirect:/authorization";
    }
}
