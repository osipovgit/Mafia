package ru.eltex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.eltex.entity.User;
import ru.eltex.repos.UserRepo;

import java.util.Map;

@Controller

public class RegistrationController {
    @Autowired
    private UserRepo userRepo;

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
        user.setReady(true);
        userRepo.save(user);
        return "redirect:/";
    }
}
