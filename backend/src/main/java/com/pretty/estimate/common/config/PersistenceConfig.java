package com.we.broke.common.config;

import com.we.broke.common.audit.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories({"com.we.broke.app.repository", "com.we.broke.auth.repository",
        "com.we.broke.payment.repository", "com.we.broke.common.repository"})
public class PersistenceConfig {

    @Bean
    AuditorAware<Long> auditorProvider() {
        return new SecurityAuditorAware();
    }

}
