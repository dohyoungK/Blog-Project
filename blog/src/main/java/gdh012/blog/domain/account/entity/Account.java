package gdh012.blog.domain.account.entity;

import gdh012.blog.domain.category.entity.Category;
import gdh012.blog.domain.comment.entity.Comment;
import gdh012.blog.domain.post.entity.Post;
import gdh012.blog.domain.tag.entity.Tag;
import gdh012.blog.global.audit.BaseTimeEntity;
import gdh012.blog.domain.account.constants.Role;
import gdh012.blog.domain.account.constants.SocialType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Column(name = "REFRESH_TOKEN", length = 1000)
    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    @OneToMany(mappedBy = "account")
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Tag> tags = new ArrayList<>();

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    @Builder(toBuilder = true)
    public Account(Long accountId, String email, String nickname, String password, String profileImageUrl, String refreshToken, Role role, SocialType socialType) {
        this.accountId = accountId;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.refreshToken = refreshToken;
        this.role = role;
        this.socialType = socialType;
    }
}
