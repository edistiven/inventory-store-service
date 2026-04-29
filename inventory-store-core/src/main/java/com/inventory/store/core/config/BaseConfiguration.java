package com.inventory.store.core.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Base spring configuration for inventory-store-core module.
 *
 * @author Generated
 * @version 1.0
 */
@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = "com.inventory.store.client.entity")
@EnableJpaRepositories(basePackages = "com.inventory.store.client.repository")
@ComponentScan(basePackages = {"com.inventory.store"})
public class BaseConfiguration {

    /**
     * Bean transactionManager.
     *
     * @param emf a {@link jakarta.persistence.EntityManagerFactory} object.
     * @return a {@link org.springframework.transaction.PlatformTransactionManager} object.
     */
    @Bean
    public PlatformTransactionManager jpaTransactionManager(EntityManagerFactory emf) {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(emf);
        return txManager;
    }
}
