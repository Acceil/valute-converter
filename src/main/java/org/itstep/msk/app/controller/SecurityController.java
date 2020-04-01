package org.itstep.msk.app.controller;

import net.bytebuddy.utility.RandomString;
import org.hibernate.id.GUIDGenerator;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.enums.Role;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class SecurityController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

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
    public String register(@ModelAttribute User user) throws Exception {
        User same = userRepository.findByEmail(user.getEmail());
        if (same != null) {
            return "redirect:/registration?same=true";
        }

        user.getRoles().add(Role.ROLE_USER);
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );
        user.setConfirmCode(generateConfirmCode());
        user.setConfirmExpired(generateConfirmExpired());
        user.setActive(false);

        userRepository.save(user);
        userRepository.flush();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", user.getEmail());
        parameters.put("confirmLink", "/confirm/" + user.getId() + "/" + user.getConfirmCode());
        mailService.send(
                user.getEmail(),
                "Подтверждение регистрации",
                "registration_mail.html",
                parameters
        );

        return "redirect:/login";
    }

    @GetMapping("/confirm/{id}/{code}") // /confirm/123/uuid-uuid-uuid
    public String confirm(@PathVariable("id") User user, @PathVariable("code") String confirmCode) {
        if (user.getConfirmCode().equals(confirmCode)
                && !user.getConfirmExpired().before(new Date()) // confirmExpired >= current date
        ) {
            user.setActive(true);
            user.setConfirmCode(generateConfirmCode());
            user.setConfirmExpired(new Date());
            userRepository.save(user);
            userRepository.flush();
        }

        return "redirect:/login";
    }

    private String generateConfirmCode() {
        return UUID.randomUUID().toString();
    }

    private Date generateConfirmExpired() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 3);

        return calendar.getTime();
    }
}
