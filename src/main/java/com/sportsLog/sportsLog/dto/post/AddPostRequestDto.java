package com.sportsLog.sportsLog.dto.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddPostRequestDto {
	private String board;
	private String title;
	private String content;
	private String email;
}