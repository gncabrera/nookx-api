package com.dot.collector.api;

import com.dot.collector.api.config.AsyncSyncConfiguration;
import com.dot.collector.api.config.DatabaseTestcontainer;
import com.dot.collector.api.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = {
        DotCollectorApp.class,
        JacksonConfiguration.class,
        AsyncSyncConfiguration.class,
        com.dot.collector.api.config.JacksonHibernateConfiguration.class,
    }
)
@ImportTestcontainers(DatabaseTestcontainer.class)
public @interface IntegrationTest {}
