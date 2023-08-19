package com.example.board.comment.dto;

        import com.example.board.comment.entity.Comment;

        import lombok.*;

        import java.time.LocalDateTime;
        import java.util.List;
        import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponseCommentDto {
    private Long id;
    private Long boardId;
    private Long userId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private Long parentCommentId;

    private List<PostResponseCommentDto> replies;

    public PostResponseCommentDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.username = comment.getUser().getUsername();
        this.userId = comment.getUser().getId();
        this.boardId = comment.getBoard().getId();
        this.parentCommentId = comment.getParent() != null ? comment.getParent().getId() : null;
        this.replies = comment.getReplies().stream()
                .map(PostResponseCommentDto::new)
                .collect(Collectors.toList());
    }

}
