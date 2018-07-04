package net.joe.itemfindnotifier.config;

import net.joe.itemfindnotifier.service.ItemParser;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.file.FileReadingMessageSource;

import java.io.File;

@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
@ComponentScan(basePackages = "net.joe.itemfindnotifier")
@EnableJpaRepositories(basePackages = "net.joe.itemfindnotifier.persistence")
public class ApplicationConfiguration {



}
