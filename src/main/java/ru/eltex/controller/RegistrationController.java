package ru.eltex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.eltex.entity.User;
import ru.eltex.repos.UserRepo;

import java.util.Map;

@Controller
public class RegistrationController {
    private final UserRepo userRepo;

    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @RequestMapping("/")
    public String signIn(Model model) {
        return "authorization.html";
    }

    @RequestMapping("/signup")
    public String signUp(Model model) {
        return "signup.html";
    }

    @PostMapping("/signup")
    public String signUpNewUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "signup";
        }
        user.setActive(true);
        user.setReady(false);
        user.setCountGame(0);
        userRepo.save(user);
        return "redirect:/";
    }
}
//TODO Не веркает вход, ссылка на регистрацию тоже