package net.joe.itemfindnotifier.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("itemfindnotifier")
public class ApplicationProperties {

    @Setter
    private String itemFileDirectory;

    public String getItemFileDirectory() {
        return itemFileDirectory.endsWith("/") ? itemFileDirectory : itemFileDirectory + "/";
    }

    @Getter
    @Setter
    private String itemFileName;

    @Getter
    @Setter
    private String discordWebhookUrl;

    @Getter
    @Setter
    private List<String> messagePrefixes;

    @Getter
    @Setter
    private List<String> filters;

}
