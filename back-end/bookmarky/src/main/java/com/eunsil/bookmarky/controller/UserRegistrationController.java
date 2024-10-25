package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.vo.UserVO;
import com.eunsil.bookmarky.service.user.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;


    @PostMapping("/registration")
    public boolean registerUser(@Valid @RequestBody UserVO userVO) {
        return userRegistrationService.registerUser(userVO);
    }


    @PostMapping("/registration/username/{username}")
    public ResponseEntity<Void> checkUsername(@PathVariable String username) {
        if (userRegistrationService.isDuplicateUsername(username)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/registration/nickname/{nickname}")
    public ResponseEntity<Void> checkNickname(@PathVariable String nickname) {
        if (userRegistrationService.isDuplicateNickname(nickname)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/registration/telephone/{telephone}")
    public ResponseEntity<Void> checkTelephone(@PathVariable String telephone) {
        if (userRegistrationService.isDuplicateTelephone(telephone)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
