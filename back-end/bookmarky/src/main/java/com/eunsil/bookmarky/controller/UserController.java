package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.config.auth.CustomUserDetailsService;
import com.eunsil.bookmarky.domain.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
=======
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
>>>>>>> b8be00a12e1c3b4474d2fcc67acb25247964b174
public class UserController {

    private final CustomUserDetailsService userDetailService;

    @Autowired
    public UserController(CustomUserDetailsService userDetailService) {
        this.userDetailService = userDetailService;
    }

<<<<<<< HEAD
=======
    @GetMapping("/")
    public String login() {
        return "ok";
    }

>>>>>>> b8be00a12e1c3b4474d2fcc67acb25247964b174
    @PostMapping("/join")
    public String join(@RequestBody UserDTO userDTO) {
        return userDetailService.join(userDTO);
    }

}
