package org.itstep.msk.app.controller;

import org.itstep.msk.app.entity.User;
import org.itstep.msk.app.entity.Valute;
import org.itstep.msk.app.entity.ValuteLike;
import org.itstep.msk.app.exception.NotFoundException;
import org.itstep.msk.app.repository.UserRepository;
import org.itstep.msk.app.repository.ValuteLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/like")
public class LikeController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValuteLikeRepository valuteLikeRepository;

    @PostMapping("/valute/{id}") // http://localhost:8080/like/valute/{id}
    public Boolean valuteLike(
            @PathVariable(name = "id") Valute valute,
            Principal principal
    ) {
        if (valute == null) {
            throw new NotFoundException();
        }

        User user = userRepository.findByEmail(principal.getName());
        ValuteLike like = valuteLikeRepository.findByUserAndValute(
                user,
                valute
        );

        if (like == null) {
            like = new ValuteLike();
            like.setUser(user);
            like.setValute(valute);
            valuteLikeRepository.save(like);
            valuteLikeRepository.flush();

            return true;
        }

        valuteLikeRepository.delete(like);
        valuteLikeRepository.flush();

        return false;
    }
}
