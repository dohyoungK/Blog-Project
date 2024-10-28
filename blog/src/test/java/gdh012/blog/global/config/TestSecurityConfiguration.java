package gdh012.blog.global.config;

import gdh012.blog.domain.account.constants.Role;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@TestConfiguration
public class TestSecurityConfiguration {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
//                                new AntPathRequestMatcher("/**"),
                                new AntPathRequestMatcher("/"),
                                new AntPathRequestMatcher("/index.html"),
                                new AntPathRequestMatcher("/account/signUp"),
                                new AntPathRequestMatcher("/h2/**")
                        ).permitAll()
                        .requestMatchers(
                                new AntPathRequestMatcher("/account/accessDeniedTest")
                        ).hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
