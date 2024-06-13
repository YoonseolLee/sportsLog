package com.sportsLog.sportsLog.controller.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

    @GetMapping("baseball/korean")
    public String koreanBaseballBoard() {
        return "baseball-korean";
    }

    @GetMapping("baseball/foreign")
    public String foreignBaseballBoard() {
        return "baseball-foreign";
    }

    @GetMapping("basketball/korean")
    public String koreanBasketballBoard() {
        return "basketball-korean.html";
    }

    @GetMapping("basketball/foreign")
    public String foreignBasketballBoard() {
        return "basketball-foreign";
    }

    @GetMapping("americanFootball")
    public String americanFootballBoard() {
        return "americanFootball";
    }

    @GetMapping("soccer/korean")
    public String koreanSoccerBoard() {
        return "soccer-korean";
    }

    @GetMapping("soccer/foreign")
    public String foreignSoccerBoard() {
        return "soccer-foreign";
    }
}

