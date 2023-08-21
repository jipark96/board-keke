package com.example.board.user.dto;

import com.example.board.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserDto {

    private Long id;
    private String email;
    private String username;
    private String password;
    private String name;

    public GetUserDto(User user)  {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password=user.getPassword();
        this.name = user.getName();
    }
}
