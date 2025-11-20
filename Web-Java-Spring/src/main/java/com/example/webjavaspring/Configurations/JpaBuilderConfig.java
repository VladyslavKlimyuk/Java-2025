//package com.example.webjavaspring.Configurations;
//
//import org.springframework.beans.factory.ObjectProvider;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
//
//import javax.sql.DataSource;
//import java.util.Map;
//import java.util.function.Function;
//
//@Configuration
//public class JpaBuilderConfig {
//
//    @Bean
//    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
//            @Qualifier("ticketJpaProperties") JpaProperties jpaProperties,
//            ObjectProvider<DataSource> dataSources,
//            ObjectProvider<PersistenceUnitManager> persistenceUnitManagers
//    ) {
//        AbstractJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
//
//        jpaVendorAdapter.setShowSql(jpaProperties.isShowSql());
//        jpaVendorAdapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
//        jpaVendorAdapter.setGenerateDdl(jpaProperties.isGenerateDdl());
//
//        Function<DataSource, Map<String, ?>> jpaPropertiesFactory = (datasource) -> {
//            @SuppressWarnings("unchecked")
//            Map<String, Object> properties = (Map<String, Object>) (Map<?, ?>) jpaProperties.getProperties();
//            return properties;
//        };
//
//        return new EntityManagerFactoryBuilder(
//                jpaVendorAdapter,
//                jpaPropertiesFactory,
//                persistenceUnitManagers.getIfAvailable()
//        );
//    }
//}