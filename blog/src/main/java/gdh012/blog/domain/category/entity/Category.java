package gdh012.blog.domain.category.entity;

import gdh012.blog.domain.board.entity.Board;
import gdh012.blog.domain.account.entity.Account;
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
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long categoryId;

    @Column(name = "Name", nullable = false, length = 50)
    private String name;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @OneToMany(mappedBy = "category")
    private List<Board> boards = new ArrayList<>();

    public void addBoard(Board board) {
        this.boards.add(board);
    }

    public void updateCategory(String name) {
        this.name = name;
    }

    @Builder(toBuilder = true)
    public Category(String name, Account account) {
        this.name = name;
        this.account = account;
    }
}
