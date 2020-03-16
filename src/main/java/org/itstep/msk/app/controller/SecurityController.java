package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.enums.Role;
import org.itstep.msk.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SecurityController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String login(@RequestParam(defaultValue = "false") String error, Model model) {
        model.addAttribute("error", error.equalsIgnoreCase("true"));

        return "login";
    }

    @GetMapping("/registration") // http://localhost:9999/registration?same=true
    public String registration(@RequestParam(defaultValue = "false") String same, Model model) {
        model.addAttribute("same", same.equalsIgnoreCase("true"));

        return "registration";
    }

    @PostMapping("/registration")
    public String register(@ModelAttribute User user) {
        User same = userRepository.findByEmail(user.getEmail());
        if (same != null) {
            return "redirect:/registration?same=true";
        }

        user.getRoles().add(Role.ROLE_USER);
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        userRepository.save(user);
        userRepository.flush();

        return "redirect:/login";
    }
}
