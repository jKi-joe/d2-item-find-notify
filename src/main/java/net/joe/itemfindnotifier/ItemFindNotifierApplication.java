package net.joe.itemfindnotifier;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.joe.itemfindnotifier.config.ApplicationConfiguration;
import net.joe.itemfindnotifier.config.ApplicationProperties;
import net.joe.itemfindnotifier.service.ItemFileListener;
import net.joe.itemfindnotifier.service.ItemParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.io.IOException;

@SpringBootApplication
@Import(ApplicationConfiguration.class)
@RequiredArgsConstructor
@Log4j2
public class ItemFindNotifierApplication implements CommandLineRunner {

    private final ApplicationProperties applicationProperties;

    private final ItemParser itemParser;

    private final ItemFileListener itemFileListener;

    public static void main(String[] args) {
        SpringApplication.run(ItemFindNotifierApplication.class, args);
    }

    @Override
    public void run(final String... args) throws IOException, InterruptedException {
        itemParser.initLastFileReadUntil(applicationProperties.getItemFileDirectory() + applicationProperties.getItemFileName());
        log.info("ItemFindNotify has started successfully");
        itemFileListener.start();
    }
}
