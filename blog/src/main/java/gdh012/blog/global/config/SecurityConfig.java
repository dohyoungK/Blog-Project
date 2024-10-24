package gdh012.blog.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import gdh012.blog.domain.account.constants.Role;
import gdh012.blog.domain.account.repository.AccountRepository;
import gdh012.blog.global.jwt.filter.JwtAuthenticationProcessingFilter;
import gdh012.blog.global.jwt.handler.JwtAccessDeniedHandler;
import gdh012.blog.global.jwt.handler.JwtAuthenticationEntryPoint;
import gdh012.blog.global.jwt.service.JwtService;
import gdh012.blog.global.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import gdh012.blog.global.login.handler.LoginFailureHandler;
import gdh012.blog.global.login.handler.LoginSuccessHandler;
import gdh012.blog.global.login.service.LoginService;
import gdh012.blog.global.oauth2.handler.OAuth2LoginFailureHandler;
import gdh012.blog.global.oauth2.handler.OAuth2LoginSuccessHandler;
import gdh012.blog.global.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity // WebSecurityConfiguration, SpringWebMvcImportSelector, OAuth2ImportSelector, HttpSecurityConfiguration 클래스 import 해주는 역할
@Configuration
public class SecurityConfig {
    private final LoginService loginService;
    private final JwtService jwtService;
    private final AccountRepository accountRepository;
    private final ObjectMapper objectMapper;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
//                .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer
//                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // disable이지만 h2 사용끝날때까지 임시로 sameorigin
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // 인증되지 않은 사용자가 인증이 필요한 요청 엔드포인트로 접근하려 할 때 발생하는 예외 처리
                        .accessDeniedHandler(new JwtAccessDeniedHandler())) // 인증 완료된 사용자가 권한이 없을 때 발생하는 예외 처리
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
//                                new AntPathRequestMatcher("/**")
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/index.html"),
                                new AntPathRequestMatcher("/account/signUp"),
                                new AntPathRequestMatcher("/h2/**")
                        ).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/account/accessDeniedTest")
                        ).hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated())
                .oauth2Login(oauth -> oauth
                        .successHandler(oAuth2LoginSuccessHandler)
                        .failureHandler(oAuth2LoginFailureHandler)
                        .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                )
                .addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
                .addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, accountRepository);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordLoginFilter;
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, accountRepository);
    }
}
