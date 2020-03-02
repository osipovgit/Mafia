package ru.eltex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.eltex.entity.User;
import ru.eltex.repos.UserRepo;

import java.util.Map;

@Controller
public class RegistrationController {
    private final UserRepo userRepo;

    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @PostMapping("/")
    public String signIn(User user, Map<String, Object> model) {
        User userRepoByUsernameAndPassword = userRepo.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (userRepoByUsernameAndPassword == null) {
            model.put("message", "User exists!");
            return "/authorization.html";
        }
        user.setActive(true);
        user.setReady(false);
        return "home.html";
    }

    @GetMapping("/signup")
    public String signUp(Model model) {
        return "signup.html";
    }

    @PostMapping("/signup")
    public String signUpNewUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsername(user.getUsername());
        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "signup.html";
        }
        user.setActive(true);
        user.setReady(false);
        user.setCountGame(0L);
        userRepo.save(user);
        return "redirect:/authorization.html";
    }
}
//TODO Не веркает вход