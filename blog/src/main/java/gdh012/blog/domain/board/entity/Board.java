package gdh012.blog.domain.board.entity;

import gdh012.blog.domain.category.entity.Category;
import gdh012.blog.domain.comment.entity.Comment;
import gdh012.blog.domain.tag.entity.Tag;
import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOARD_ID")
    private Long boardId;

    @Column(name = "TITLE", nullable = false, length = 50)
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "CATOGORY_ID")
    private Category category;

    public void updateBoard(String title, String content) {
        if (!title.equals("")) this.title = title;
        if (!content.equals("")) this.content = content;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    @Builder(toBuilder = true)
    public Board(Long boardId, String title, String content, Account account) {
        this.boardId = boardId;
        this.title = title;
        this.content = content;
        this.account = account;
    }
}
