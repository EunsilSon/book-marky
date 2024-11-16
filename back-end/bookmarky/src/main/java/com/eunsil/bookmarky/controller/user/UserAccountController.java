package com.eunsil.bookmarky.controller.user;

import com.eunsil.bookmarky.domain.vo.PasswordVO;
import com.eunsil.bookmarky.service.user.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping("/user/mail/{username}")
    public ResponseEntity<String> emailForResetPassword(@PathVariable String username) {
        if (userAccountService.sendResetEmailWithToken(username)) {
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/user")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordVO passwordVO) {
        if (userAccountService.resetPwWithToken(passwordVO)) {
            return ResponseEntity.status(HttpStatus.OK).body("ok");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token validation failed");
    }

    @GetMapping("/user/nickname")
    public ResponseEntity<String> getNickname() {
        String nickname = userAccountService.getNickname();
        if (nickname == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(nickname);
    }

}