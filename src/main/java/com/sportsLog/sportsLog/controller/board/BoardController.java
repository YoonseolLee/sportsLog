package com.sportsLog.sportsLog.controller.board;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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

    @GetMapping("/createPost")
    public ModelAndView createPost(@RequestParam("board") String board) {
        ModelAndView mav = new ModelAndView("createPost");
        mav.addObject("board", board);
        return mav;
    }

    // TODO: JSON으로 수정
    @PostMapping("/{board}/createPost")
    public RedirectView createPost(@PathVariable("board") String board,
        @RequestParam("title") String title,
        @RequestParam("content") String content,
        @RequestParam(value = "file", required = false) MultipartFile file) {
        // 게시글 저장 로직 추가 (예: 서비스 호출)

        // 게시글 저장 후 해당 게시판 페이지로 리디렉션
        return new RedirectView("/board/" + board);
    }
}

