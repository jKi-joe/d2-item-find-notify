package net.joe.itemfindnotifier.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("net.joe")
@Getter
@Setter
public class ApplicationProperties {

    private String itemFileDirectory;

    private String itemFileName;

    private String discordWebhookUrl;

    private List<String> messagePrefixes;

    private List<String> filters;

}
