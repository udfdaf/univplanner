package com.doit.univplanner.dto;

import com.doit.univplanner.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    @NotBlank(message = "사용자 이름은 필수입니다.")
    @Size(min = 4, max = 20, message = "사용자 이름은 4-20자 사이여야 합니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 20, message = "비밀번호는 8-20자 사이여야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String passwordCheck;

    public User toEntity(String encodedPassword) {
        User user = new User();
        user.setUsername(this.username);
        user.setPassword(encodedPassword);
        return user;
    }
}