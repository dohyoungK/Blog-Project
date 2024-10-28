package gdh012.blog.global.auth.oauth2;

import gdh012.blog.domain.account.constants.Role;
import gdh012.blog.domain.account.constants.SocialType;
import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.global.auth.oauth2.userInfo.OAuth2UserInfo;
import gdh012.blog.global.auth.oauth2.userInfo.GoogleOAuth2UserInfo;
import gdh012.blog.global.auth.oauth2.userInfo.KakaoOAuth2UserInfo;
import gdh012.blog.global.auth.oauth2.userInfo.NaverOAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private String nameAttributeKey;
    private OAuth2UserInfo oauth2UserInfo;

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType,
                                     String userNameAttributeName, Map<String, Object> attributes) {
        if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        if (socialType == SocialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public Account toEntity(SocialType socialType, OAuth2UserInfo oauth2UserInfo) {
        return Account.builder()
                .socialType(socialType)
                .email(oauth2UserInfo.getId() + "@" + socialType.name() + ".com") // 소셜 계정의 식별자를 활용해 email 생성
                .nickname(oauth2UserInfo.getNickname())
                .password(socialType.name() + oauth2UserInfo.getId())
                .profileImageUrl(oauth2UserInfo.getProfileImageUrl())
                .role(Role.GUEST)
                .build();
    }
}
