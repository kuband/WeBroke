package com.backend.common.config;

import com.backend.common.audit.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories({"com.backend.app.repository", "com.backend.auth.repository",
        "com.backend.payment.repository", "com.backend.common.repository"})
public class PersistenceConfig {

    @Bean
    AuditorAware<Long> auditorProvider() {
        return new SecurityAuditorAware();
    }

}
