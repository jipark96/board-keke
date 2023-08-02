package com.example.board.user.dto;

import com.example.board.user.entity.User;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinResponseDto {
    private Long id;
    private String email;
    private String username;
    private String name;

    public static JoinResponseDto of (User user){
        return JoinResponseDto
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }


}
