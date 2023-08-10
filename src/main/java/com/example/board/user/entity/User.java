package com.example.board.user.entity;

import com.example.board.board.entity.Board;
import com.example.board.comment.entity.Comment;
import com.example.board.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false, length = 10)
    private String name;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Board> boardList = new ArrayList<Board>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Comment> commentList = new ArrayList<Comment>();

    public String getUsername() {
        return this.username;
    }
}
