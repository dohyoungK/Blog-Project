package gdh012.blog.global.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import gdh012.blog.domain.account.repository.AccountRepository;
import gdh012.blog.global.auth.config.SecurityConfig;
import gdh012.blog.global.auth.jwt.service.JwtService;
import gdh012.blog.global.auth.login.service.LoginService;
import gdh012.blog.global.auth.oauth2.handler.OAuth2LoginFailureHandler;
import gdh012.blog.global.auth.oauth2.handler.OAuth2LoginSuccessHandler;
import gdh012.blog.global.auth.oauth2.service.CustomOAuth2UserService;
import gdh012.blog.global.config.RestDocsConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@Import({SecurityConfig.class, RestDocsConfiguration.class})
@ExtendWith(RestDocumentationExtension.class)
public class TestSetUpUtil {
    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private LoginService loginService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @MockBean
    private OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @MockBean
    private CustomOAuth2UserService customOAuth2UserService;

    @BeforeEach
    public void setUp(final WebApplicationContext context, final RestDocumentationContextProvider provider) {
        this.mockMvc = webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .apply(documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .build();
    }
}
