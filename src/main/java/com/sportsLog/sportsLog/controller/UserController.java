package com.sportsLog.sportsLog.controller;

import com.sportsLog.sportsLog.dto.AddUserRequestDto;
import com.sportsLog.sportsLog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/signup")
    public String showSignupForm() {
        return "signup";
    }

    @PostMapping("/user/signup")
    public String signup(AddUserRequestDto addUserRequest) {
        userService.saveUser(addUserRequest);
        return "redirect:/signup"; // 임시
    }
}
