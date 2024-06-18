package com.sportsLog.sportsLog.controller.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/board")
public class BoardController {

    @GetMapping("/baseball")
    public String baseballBoard() {
        return "/board/baseball";
    }

    @GetMapping("/basketball")
    public String basketballBoard() {
        return "/board/basketball";
    }

    @GetMapping("/soccer")
    public String soccerBoard() {
        return "/board/soccer";
    }

    @GetMapping("/americanFootball")
    public String americanFootballBoard() {
        return "/board/americanFootball";
    }

    @GetMapping("/mma")
    public String mmaBoard() {
        return "/board/mma";
    }

    @GetMapping("/boxing")
    public String boxingBoard() {
        return "/board/boxing";
    }

}

