package com.sportsLog.sportsLog.dto.board;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddBoardRequestDto {

	@NotNull(message = "게시판 이름은 필수 입력값입니다.")
	@Size(min = 2, max = 20, message = "게시판 이름은 2자 이상 20자 이하여야 합니다.")
	private String name;

	@Size(max = 100, message = "게시판 설명은 100자 이하여야 합니다.")
	private String description;
}
