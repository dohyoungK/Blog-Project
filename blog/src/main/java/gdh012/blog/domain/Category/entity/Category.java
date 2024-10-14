package gdh012.blog.domain.Category.entity;

import gdh012.blog.domain.Comment.entity.Comment;
import gdh012.blog.domain.Post.entity.Post;
import gdh012.blog.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.AccessLevel;
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
    private List<Post> posts = new ArrayList<>();
}
