package org.itstep.msk.app.controller.admin;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.enums.Role;
import org.itstep.msk.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable(name = "id") User user,
            Model model
    ) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "admin/user/edit";
    }

    @PostMapping("/save/{id}")
    public String save(
             @PathVariable(name = "id") User user,
             @ModelAttribute User editedUser
    ) {
        if (editedUser.getEmail().equals("admin")) {
            editedUser.getRoles().add(Role.ROLE_ADMIN);
        }

        user.setEmail(editedUser.getEmail());
        user.getRoles().clear();
        for (Role role : editedUser.getRoles()) {
            user.getRoles().add(role);
        }

        userRepository.save(user);
        userRepository.flush();

        return "redirect:/admin/user/edit/" + user.getId();
    }
}
