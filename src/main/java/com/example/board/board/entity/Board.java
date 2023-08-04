package com.example.board.board.entity;

import com.example.board.comment.entity.Comment;
import com.example.board.common.entity.BaseEntity;
import com.example.board.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Board extends BaseEntity {

    @Id
    @Column(name = "board_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_title", nullable = false, length = 45)
    private String title;

    @Column(name = "board_content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    List<Comment> commentList = new ArrayList<Comment>();

    public void setUser(User user) {
        this.user = user;
    }

    @Builder
    public Board(Long id, String title, String content, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        if (user != null) {
            user.getBoardList().add(this);
        }
    }

    public void updateBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void deleteBoard() {
        this.state = State.INACTIVE;
    }

    public void addComment(Comment comment) {
        comment.setBoard(this);
        commentList.add(comment);
    }
}
