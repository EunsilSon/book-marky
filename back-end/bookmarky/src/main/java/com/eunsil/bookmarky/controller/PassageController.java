package com.eunsil.bookmarky.controller;

import com.eunsil.bookmarky.domain.request.PassageReq;
import com.eunsil.bookmarky.service.PassageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/passage")
public class PassageController {

    private final PassageService passageService;

    public PassageController(PassageService passageService) {
        this.passageService = passageService;
    }

    @PostMapping("/")
    public ResponseEntity add(@Valid @RequestBody PassageReq passageReq) throws Exception {
        return passageService.add(passageReq);
    }

}
