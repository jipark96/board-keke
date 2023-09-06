package com.example.board.board.entity;

import com.example.board.comment.entity.Comment;
import com.example.board.common.entity.BaseEntity;
import com.example.board.file.entity.File;
import com.example.board.like.entity.Like;
import com.example.board.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
public class Board extends BaseEntity {

    @Id
    @Column(name = "board_id", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "board_title", nullable = false, length = 45)
    private String title;

    @Column(name = "board_content", nullable = false)
    @Lob
    private String content;

    @Column
    private String username;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    List<Comment> commentList = new ArrayList<Comment>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<File> fileList = new ArrayList<>();

    public void setUser(User user) {
        this.user = user;
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
    public List<File> getFileList() {
        return this.fileList;
    }

    public void increaseLikeCount() {
        this.likeCount += 1;
    }
    public void decreaseLikeCount() {
        this.likeCount -= 1;
    }
}
