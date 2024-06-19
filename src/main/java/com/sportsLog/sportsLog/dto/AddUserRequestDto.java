package com.sportsLog.sportsLog.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddUserRequestDto {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "인증 번호는 필수 입력 값입니다.")
    private String authNumber;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
        message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자리여야 합니다."
    )
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수 입력 값입니다.")
    private String confirmPassword;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(
        regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$",
        message = "닉네임은 2자 이상 16자 이하, 영어 또는 숫자 또는 한글로 구성되어야 합니다."
    )
    private String nickname;

    @NotNull(message = "생년월일은 필수 입력 값입니다.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate birthdate;

    private boolean emailVerified;
}

