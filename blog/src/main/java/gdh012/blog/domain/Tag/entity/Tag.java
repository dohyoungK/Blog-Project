package gdh012.blog.domain.Tag.entity;

import gdh012.blog.domain.Post.entity.Post;
import gdh012.blog.domain.account.entity.Account;
import jakarta.persistence.*;
import lombok.AccessLevel;
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

    @Column(name = "COUNT", nullable = false)
    private Long count;

    @ManyToOne
    @JoinColumn(name = "ACCOUNT_ID")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;
}
