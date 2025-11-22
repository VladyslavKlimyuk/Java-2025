//package com.example.webjavaspring.Configurations;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//import java.util.Objects;
//import java.util.Map;
//
//@Configuration
//@EnableJpaRepositories(
//        basePackages = {},
//        entityManagerFactoryRef = "bookEntityManagerFactory",
//        transactionManagerRef = "bookTransactionManager"
//)
//public class BookDataSourceConfig {
//
//    @Bean(name = "bookDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.book")
//    public DataSource bookDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "bookJpaProperties")
//    @ConfigurationProperties(prefix = "spring.jpa.book")
//    public JpaProperties bookJpaProperties() {
//        return new JpaProperties();
//    }
//
//    @Bean(name = "bookEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean bookEntityManagerFactory(
//            EntityManagerFactoryBuilder builder,
//            @Qualifier("bookDataSource") DataSource dataSource,
//            @Qualifier("bookJpaProperties") JpaProperties jpaProperties) {
//
//        @SuppressWarnings("unchecked")
//        Map<String, Object> vendorProperties = (Map<String, Object>) (Map<?, ?>) jpaProperties.getProperties();
//
//        vendorProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//
//        return builder
//                .dataSource(dataSource)
//                .packages("")
//                .properties(vendorProperties)
//                .persistenceUnit("book")
//                .build();
//    }
//
//    @Bean(name = "bookTransactionManager")
//    public PlatformTransactionManager bookTransactionManager(
//            @Qualifier("bookEntityManagerFactory") LocalContainerEntityManagerFactoryBean bookEntityManagerFactory) {
//        return new JpaTransactionManager(Objects.requireNonNull(bookEntityManagerFactory.getObject()));
//    }
//}