package com.sportsLog.sportsLog.service.board;

import org.springframework.stereotype.Service;

import com.sportsLog.sportsLog.dto.board.AddBoardRequestDto;
import com.sportsLog.sportsLog.entity.Board;
import com.sportsLog.sportsLog.repository.BoardRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {

	private final BoardRepository boardRepository;

	@Transactional
	public void saveBoard(AddBoardRequestDto addBoardRequestDto) {
		// validate board 추가 예정

		Board board = Board.builder()
			.name(addBoardRequestDto.getName())
			.description(addBoardRequestDto.getDescription())
			.build();

		boardRepository.save(board);
	}
}
