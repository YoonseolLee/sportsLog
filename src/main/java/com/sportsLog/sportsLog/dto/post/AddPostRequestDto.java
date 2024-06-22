package com.sportsLog.sportsLog.dto.post;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPostRequestDto {

	@NotNull(message = "게시판은 필수 입력값입니다.")
	private String board;

	@NotBlank(message = "제목은 필수 입력값입니다.")
	@Size(min = 1, max = 100, message = "제목은 최소 1자, 최대 100자 입력할 수 있습니다.")
	private String title;

	@NotBlank(message = "내용은 필수 입력값입니다.")
	@Size(min = 2, max = 10000, message = "내용은 최소 2자, 최대 10,000자 입력할 수 있습니다.")
	private String content;

	@Email(message = "올바른 이메일 형식이 아닙니다.")
	@NotBlank(message = "이메일은 필수 입력값입니다.")
	private String email;
}