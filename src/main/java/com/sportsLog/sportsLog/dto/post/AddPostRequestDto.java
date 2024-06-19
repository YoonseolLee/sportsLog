package com.sportsLog.sportsLog.dto.post;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddPostRequestDto {

	private String title;
	private String content;
	private String board;
	private String filePath;
}
