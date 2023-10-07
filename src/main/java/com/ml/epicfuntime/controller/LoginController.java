package com.ml.epicfuntime.controller;

import com.ml.epicfuntime.model.Role;
import com.ml.epicfuntime.model.User;
import com.ml.epicfuntime.repository.RoleRepository;
import com.ml.epicfuntime.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.regex.Pattern;

@Controller
public class LoginController {
    private final PasswordEncoder encoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public LoginController(PasswordEncoder encoder, RoleRepository roleRepository, UserRepository userRepository) {
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/createAccount")
    public String createAccount() {
        return "createAccount";
    }

    @PostMapping("/createAccount")
    public String handleCreateAccount(@RequestParam(name = "username") String username,
                                      @RequestParam(name = "password") String password,
                                      @RequestParam(name = "reEnterPassword") String reEnterPassword,
                                      @RequestParam(name = "email") String email,
                                      RedirectAttributes redirectAttributes) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "\"" +username +"\"" + " is already taken. Please enter another username.");
            return "redirect:/createAccount";
        } else if (!password.equals(reEnterPassword)) {
            redirectAttributes.addFlashAttribute("error", "Passwords do not match. Please try again.");
            return "redirect:/createAccount";
        } else if (!isValidEmail(email)) {
            redirectAttributes.addFlashAttribute("error", "\"" + email + "\"" + " is an invalid email. Please enter another email.");
            return "redirect:/createAccount";
        } else if (userRepository.findByEmail(email).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "\"" + email + "\"" + " is already being used. Please enter another email.");
            return "redirect:/createAccount";
        } else {
            userRepository.save(new User(username, email, encoder.encode(password)));
            roleRepository.save(new Role(username, "ROLE_USER"));
        }
        return "/success";

    }

    private static Boolean isValidEmail(String email) {
        return Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$").matcher(email).matches();
    }
}
