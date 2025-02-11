package gdh012.blog.domain.comment.entity;

import gdh012.blog.domain.board.entity.Board;
import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long commentId;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "board_ID")
    private Board board;

    public void updateComment(String content) {
        this.content = content;
    }

    @Builder(toBuilder = true)
    public Comment(String content, Account account, Board board) {
        this.content = content;
        this.account = account;
        this.board = board;
    }
}
