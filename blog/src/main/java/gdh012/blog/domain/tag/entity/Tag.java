package gdh012.blog.domain.tag.entity;

import gdh012.blog.domain.board.entity.Board;
import gdh012.blog.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_ID")
    private Long tagId;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "board_ID")
    private Board board;

    public void updateTag(String name) {
        this.name = name;
    }

    @Builder(toBuilder = true)
    public Tag(String name, Account account, Board board) {
        this.name = name;
        this.account = account;
        this.board = board;
    }
}
