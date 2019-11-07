package com.quemepongo.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableSpringConfigured
@EnableTransactionManagement
public class TestConf {

    @Bean
    @Primary
    public DataSource dataSource(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
        driverManagerDataSource.setPassword("0bf1122a91357958234fe17eeeaf58946ecb6a74fa19f9d33a39dc96722773ba");
        driverManagerDataSource.setUsername("erqpekgyepijit");
        driverManagerDataSource.setUrl("jdbc:postgresql://ec2-23-23-80-20.compute-1.amazonaws.com:5432/d49oddv5rl0fut");
        return driverManagerDataSource;
    }

}