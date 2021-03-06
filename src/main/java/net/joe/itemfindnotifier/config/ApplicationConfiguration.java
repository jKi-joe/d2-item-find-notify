package net.joe.itemfindnotifier.config;

import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
@ComponentScan(basePackages = "net.joe.itemfindnotifier")
public class ApplicationConfiguration {

    @Bean
    public OkHttpClient okHttpClient() {
        return  new OkHttpClient();
    }

}
