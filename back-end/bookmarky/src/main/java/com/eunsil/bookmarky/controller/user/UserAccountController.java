package com.eunsil.bookmarky.controller.user;

import com.eunsil.bookmarky.domain.vo.PasswordVO;
import com.eunsil.bookmarky.response.ApiResponse;
import com.eunsil.bookmarky.response.ResponseUtil;
import com.eunsil.bookmarky.service.user.UserAccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserAccountController {

    private final UserAccountService userAccountService;

    @GetMapping("/user/mail/{username}")
    public ApiResponse<String> emailForResetPassword(@PathVariable String username) {
        if (userAccountService.sendResetEmailWithToken(username)) {
            return ResponseUtil.createSuccessResponse();
        }
        return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, "Password Reset Email Request Failed.");
    }

    @PostMapping("/user")
    public ApiResponse<String> resetPassword(@Valid @RequestBody PasswordVO passwordVO) {
        if (userAccountService.resetPwWithToken(passwordVO)) {
            return ResponseUtil.createSuccessResponse();
        }
        return ResponseUtil.createErrorResponse(HttpStatus.UNAUTHORIZED, "Token Validation Failed.");
    }

    @GetMapping("/user/nickname")
    public ApiResponse<String> getNickname() {
        String nickname = userAccountService.getNickname();
        if (nickname.isEmpty()) {
            return ResponseUtil.createErrorResponse(HttpStatus.NOT_FOUND, "Nickname Not Found.");
        }
        return ResponseUtil.createSuccessResponse(nickname);
    }

}