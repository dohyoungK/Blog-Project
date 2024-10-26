package gdh012.blog.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class JpaAuditingConfig { // test를 위해 메인 클래스에서 @EnableJpaAuditing 분리
}
