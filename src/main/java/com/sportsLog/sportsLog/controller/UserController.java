package com.sportsLog.sportsLog.controller;

import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
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
}
