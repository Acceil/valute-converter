package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.Upload;
import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.enums.Role;
import org.itstep.msk.app.exception.NotFoundException;
import org.itstep.msk.app.repository.UploadRepository;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
public class SecurityController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UploadRepository uploadRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Value("${app.uploads.path}")
    private String uploadsPath;

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

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        User user = userRepository.findByEmail(authentication.getName());

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "admin/user/edit";
    }

    @PostMapping("/avatar")
    public String avatar(Authentication authentication, @RequestParam("file") MultipartFile file) throws IOException {
        User user = userRepository.findByEmail(authentication.getName());

        String filename = UUID.randomUUID().toString();
        Path path = Paths.get(uploadsPath).toAbsolutePath().resolve(filename);

        file.transferTo(path.toFile());

        Upload upload = new Upload();
        upload.setFilename(filename);
        upload.setOriginalFilename(file.getOriginalFilename());
        upload.setContentType(file.getContentType());

        uploadRepository.save(upload);
        uploadRepository.flush();

        user.setAvatar(upload);
        userRepository.save(user);
        userRepository.flush();

        return "redirect:/profile";
    }

    @GetMapping("/uploads/{uploadId}")
    @ResponseBody
    public ResponseEntity<Resource> upload(@PathVariable("uploadId") Upload upload) throws MalformedURLException {
        Path path = Paths.get(uploadsPath).toAbsolutePath().resolve(upload.getFilename());
        Resource file = new UrlResource(path.toUri());

        if (!file.exists() || !file.isReadable()) {
            throw new NotFoundException();
        }

        return ResponseEntity.ok().body(file);
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
