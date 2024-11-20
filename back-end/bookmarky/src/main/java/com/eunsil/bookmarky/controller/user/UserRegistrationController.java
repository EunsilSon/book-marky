package com.eunsil.bookmarky.controller.user;

import com.eunsil.bookmarky.domain.vo.UserVO;
import com.eunsil.bookmarky.response.ApiResponse;
import com.eunsil.bookmarky.response.ResponseUtil;
import com.eunsil.bookmarky.service.user.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;


    @PostMapping("/registration")
    public ApiResponse<String> registerUser(@Valid @RequestBody UserVO userVO) {
        if (userRegistrationService.registerUser(userVO)) {
            return ResponseUtil.createSuccessResponse();
        }
        return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, "Failed to Create Security Question.");
    }

    @PostMapping("/registration/username/{username}")
    public ApiResponse<String> checkUsername(@PathVariable String username) {
        if (userRegistrationService.isDuplicateUsername(username)) {
            return ResponseUtil.createErrorResponse(HttpStatus.CONFLICT, "Username already exists.");
        }
        return ResponseUtil.createSuccessResponse();
    }

    @PostMapping("/registration/nickname/{nickname}")
    public ApiResponse<String> checkNickname(@PathVariable String nickname) {
        if (userRegistrationService.isDuplicateNickname(nickname)) {
            return ResponseUtil.createErrorResponse(HttpStatus.CONFLICT, "Nickname already exists.");
        }
        return ResponseUtil.createSuccessResponse();
    }

    @PostMapping("/registration/telephone/{telephone}")
    public ApiResponse<String> checkTelephone(@PathVariable String telephone) {
        if (userRegistrationService.isDuplicateTelephone(telephone)) {
            return ResponseUtil.createErrorResponse(HttpStatus.CONFLICT, "Telephone already exists.");
        }
        return ResponseUtil.createSuccessResponse();
    }

}
