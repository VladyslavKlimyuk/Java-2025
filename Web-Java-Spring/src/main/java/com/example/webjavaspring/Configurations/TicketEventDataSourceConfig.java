//package com.example.webjavaspring.Configurations;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.orm.jpa.vendor.Database;
//
//import javax.sql.DataSource;
//import java.util.Objects;
//import java.util.Map;
//
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories(
//        basePackages = "com.example.webjavaspring.Repositories",
//        entityManagerFactoryRef = "ticketEntityManagerFactory",
//        transactionManagerRef = "ticketTransactionManager"
//)
//public class TicketEventDataSourceConfig {
//
//    @Primary
//    @Bean(name = "ticketDataSource")
//    @ConfigurationProperties(prefix = "spring.datasource.ticket")
//    public DataSource ticketDataSource() {
//        return DataSourceBuilder.create().build();
//    }
//
//    @Primary
//    @Bean(name = "ticketJpaProperties")
//    @ConfigurationProperties(prefix = "spring.jpa.ticket")
//    public JpaProperties ticketJpaProperties() {
//        return new JpaProperties();
//    }
//
//    @Primary
//    @Bean(name = "ticketJpaVendorAdapter")
//    public JpaVendorAdapter ticketJpaVendorAdapter(
//            @Qualifier("ticketJpaProperties") JpaProperties jpaProperties) {
//
//        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
//        adapter.setDatabase(Database.POSTGRESQL);
//        adapter.setShowSql(jpaProperties.isShowSql());
//        adapter.setGenerateDdl(jpaProperties.isGenerateDdl());
//
//        return adapter;
//    }
//
//    @Primary
//    @Bean(name = "ticketEntityManagerFactory")
//    public LocalContainerEntityManagerFactoryBean ticketEntityManagerFactory(
//            EntityManagerFactoryBuilder builder,
//            @Qualifier("ticketDataSource") DataSource dataSource,
//            @Qualifier("ticketJpaProperties") JpaProperties jpaProperties,
//            @Qualifier("ticketJpaVendorAdapter") JpaVendorAdapter jpaVendorAdapter) {
//
//        @SuppressWarnings("unchecked")
//        Map<String, Object> vendorProperties = (Map<String, Object>) (Map<?, ?>) jpaProperties.getProperties();
//
//        vendorProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//
//        return builder
//                .dataSource(dataSource)
//                .packages("com.example.webjavaspring.Entities")
//                .properties(vendorProperties)
//                //.jpaVendorAdapter(jpaVendorAdapter)
//                .persistenceUnit("ticket")
//                .build();
//    }
//
//    @Primary
//    @Bean(name = "ticketTransactionManager")
//    public PlatformTransactionManager ticketTransactionManager(
//            @Qualifier("ticketEntityManagerFactory") LocalContainerEntityManagerFactoryBean ticketEntityManagerFactory) {
//        return new JpaTransactionManager(Objects.requireNonNull(ticketEntityManagerFactory.getObject()));
//    }
//}