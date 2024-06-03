package com.sportsLog.sportsLog.controller;

import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.service.UserService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/user/signup")
    public String showSignupForm() {
        return "/user/signup";
    }

    @PostMapping("/user/signup")
    public String saveUser(AddUserRequestDto addUserRequestDto) {
        userService.addUser(addUserRequestDto);
        return "redirect:/user/signup";
    }

    @PostMapping("/mailDuplicationValidation")
    public ResponseEntity<Void> validateMailDuplication(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        boolean isMailDuplicated = userService.checkMailDuplication(email);

        if (isMailDuplicated) {
            log.error("메일 중복: {}", email);
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok().build();
    }
}
