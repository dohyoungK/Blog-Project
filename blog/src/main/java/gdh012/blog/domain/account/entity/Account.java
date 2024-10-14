package gdh012.blog.domain.account.entity;

import gdh012.blog.domain.category.entity.Category;
import gdh012.blog.domain.comment.entity.Comment;
import gdh012.blog.domain.post.entity.Post;
import gdh012.blog.domain.tag.entity.Tag;
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
public class Account extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID")
    private Long accountId;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 100)
    private String email;

    @Column(name = "NICKNAME", nullable = false, length = 50)
    private String nickname;

    @Column(name = "PASSWORD", nullable = false, length = 100)
    private String password;

    @Column(name = "PROFILE_IMAGE_URL")
    private String profileImageUrl;

    @OneToMany(mappedBy = "account")
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Tag> tags = new ArrayList<>();
}
