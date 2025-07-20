package com.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ApplicationTests {

    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> {
            ConfigurableApplicationContext context = new SpringApplicationBuilder(Application.class)
                    .properties("spring.autoconfigure.exclude=" +
                            "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                            "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration")
                    .run();
            context.close();
        });
    }
}
