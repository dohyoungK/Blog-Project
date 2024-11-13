package gdh012.blog.global.auth.oauth2.service;

import gdh012.blog.domain.account.constants.SocialType;
import gdh012.blog.domain.account.entity.Account;
import gdh012.blog.domain.account.repository.AccountRepository;
import gdh012.blog.global.auth.oauth2.CustomOAuth2User;
import gdh012.blog.global.auth.oauth2.OAuthAttributes;
import gdh012.blog.global.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private static final String KAKAO = "kakao";
    private static final String NAVER = "naver";

    private final AccountRepository accountRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("CustomOAuth2UserService.loadUser() 실행 - OAuth2 로그인 요청 진입");

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialType socialType = getSocialType(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // nameAttributeKey가 됨, OAuth2 로그인 시 키(PK)가 되는 값
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialType, userNameAttributeName, attributes);

        Account createdAccount = getAccount(extractAttributes, socialType);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdAccount.getRole().getKey())), // 객체 한개만 저장가능한 컬렉션
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdAccount.getEmail(),
                createdAccount.getRole()
        );
    }

    private SocialType getSocialType(String registrationId) {
        if(KAKAO.equals(registrationId)) {
            return SocialType.KAKAO;
        }
        if(NAVER.equals(registrationId)) {
            return SocialType.NAVER;
        }
        return SocialType.GOOGLE;
    }

    private Account getAccount(OAuthAttributes attributes, SocialType socialType) {
        String email = attributes.getOauth2UserInfo().getId() + "@" + socialType.name() + ".com";
        Account findAccount = accountRepository.findByEmail(email).orElse(null);

        if (findAccount == null) {
            return saveAccount(attributes, socialType);
        }
        return findAccount;
    }

    private Account saveAccount(OAuthAttributes attributes, SocialType socialType) {
        Account createdAccount = attributes.toEntity(socialType, attributes.getOauth2UserInfo());
        createdAccount.toBuilder()
                .password(PasswordUtil.generateRandomPassword())
                .build();

        return accountRepository.save(createdAccount);
    }

}
