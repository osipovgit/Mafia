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

@Controller
public class RegistrationController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepo userRepo;

    public RegistrationController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

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

    @GetMapping("/signup")
    public String signUp(Model model) {
        return "signup.html";
    }

    @PostMapping("/signup")
    public String signUpNewUser(User user, Map<String, Object> model) {
        User userFromDb = userRepo.findByUsernameOrId(user.getUsername(), null);
        if (userFromDb != null) {
            model.put("message", "User exists!");
            return "signup.html";
        }
        user.setCountGame(0L);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setGameMode(0);
        userRepo.save(user);
        return "redirect:/authorization";
    }
}
//TODO НЕ ТРОГАТЬ, найти специально обученного человека, который шарит