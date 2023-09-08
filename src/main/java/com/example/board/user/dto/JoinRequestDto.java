package com.example.board.user.dto;

import com.example.board.user.entity.Role;
import com.example.board.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestDto {
    private String email;
    private String username;
    private String password;
    private String name;


    public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {

        return User.builder()
                .email(this.email)
                .username(this.username)
                .password(bCryptPasswordEncoder.encode(this.password))
                .name(this.name)
                .role(Role.USER)
                .build();
    }
}
