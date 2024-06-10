package com.sportsLog.sportsLog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.sportsLog.sportsLog.common.SessionConst;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@GetMapping("/")
	public String showMainPage(HttpSession session, Model model) {
		model.addAttribute("loginEmail", session.getAttribute(SessionConst.LOGIN_EMAIL));
		return "main/main";
	}
}
