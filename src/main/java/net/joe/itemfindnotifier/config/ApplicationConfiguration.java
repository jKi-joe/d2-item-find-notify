package net.joe.itemfindnotifier.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import okhttp3.OkHttpClient;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
@ComponentScan(basePackages = "net.joe.itemfindnotifier")
@EnableJpaRepositories(basePackages = "net.joe.itemfindnotifier.persistence")
public class ApplicationConfiguration {

    @Bean
    public OkHttpClient okHttpClient() {
        return  new OkHttpClient();
    }

}
