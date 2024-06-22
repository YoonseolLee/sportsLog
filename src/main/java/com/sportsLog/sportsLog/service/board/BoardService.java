package com.sportsLog.sportsLog.service.board;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.sportsLog.sportsLog.dto.board.AddBoardRequestDto;
import com.sportsLog.sportsLog.entity.Board;
import com.sportsLog.sportsLog.exception.BoardException;
import com.sportsLog.sportsLog.repository.BoardRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;

	@Transactional
	public void saveBoard(AddBoardRequestDto addBoardRequestDto) {
		validateBoardDuplication(addBoardRequestDto.getName());

		Board board = Board.builder()
			.name(addBoardRequestDto.getName())
			.description(addBoardRequestDto.getDescription())
			.status(addBoardRequestDto.getStatus())
			.build();

		boardRepository.save(board);
	}

	public Board findByName(String name) {
		return boardRepository.findByName(name)
			.orElseThrow(() -> new BoardException("찾을 수 없는 게시판입니다."));
	}

	private void validateBoardDuplication(String name) {
		if (boardRepository.findByName(name).equals(Optional.empty())) {
			throw new BoardException("이미 존재하는 게시판 이름입니다.");
		}
	}
}
