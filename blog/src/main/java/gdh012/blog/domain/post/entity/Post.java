package gdh012.blog.domain.post.entity;

import gdh012.blog.domain.category.entity.Category;
import gdh012.blog.domain.comment.entity.Comment;
import gdh012.blog.domain.tag.entity.Tag;
import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.global.audit.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long postId;

    @Column(name = "TITLE", nullable = false, length = 50)
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "CATOGORY_ID")
    private Category category;
}
