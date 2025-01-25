package com.eunsil.bookmarky.controller.user;

import com.eunsil.bookmarky.domain.vo.UserVO;
import com.eunsil.bookmarky.global.response.ApiResponse;
import com.eunsil.bookmarky.global.response.ResponseUtil;
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
        return ResponseUtil.createErrorResponse(HttpStatus.BAD_REQUEST, "Failed Register User");
    }

    @PostMapping("/registration/username")
    public ApiResponse<String> checkUsername(@RequestBody String username) {
        if (userRegistrationService.isUsernameDuplicate(username)) {
            return ResponseUtil.createErrorResponse(HttpStatus.CONFLICT, "Username already exists.");
        }
        return ResponseUtil.createSuccessResponse();
    }

    @PostMapping("/registration/nickname")
    public ApiResponse<String> checkNickname(@RequestBody String nickname) {
        System.out.println(nickname);
        if (userRegistrationService.isNicknameDuplicate(nickname)) {
            return ResponseUtil.createErrorResponse(HttpStatus.CONFLICT, "Nickname already exists.");
        }
        return ResponseUtil.createSuccessResponse();
    }

    @PostMapping("/registration/telephone")
    public ApiResponse<String> checkTelephone(@RequestBody String telephone) {
        if (userRegistrationService.isTelephoneDuplicate(telephone)) {
            return ResponseUtil.createErrorResponse(HttpStatus.CONFLICT, "Telephone already exists.");
        }
        return ResponseUtil.createSuccessResponse();
    }

}
