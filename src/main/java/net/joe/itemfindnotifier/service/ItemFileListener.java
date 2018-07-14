package net.joe.itemfindnotifier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.joe.itemfindnotifier.config.ApplicationProperties;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ItemFileListener {

    private final ApplicationProperties applicationProperties;

    private final ItemParser itemParser;

    private final DiscordNotifier discordNotifier;

    public void start() throws IOException, InterruptedException {
        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path folderToWatch = Paths.get(applicationProperties.getItemFileDirectory());
            folderToWatch.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
            WatchKey watchKey;
            List<String> filters = applicationProperties.getFilters();
            log.info(String.format("Watching directory '%s' for file '%s' now", applicationProperties.getItemFileDirectory(), applicationProperties.getItemFileName()));
            while ((watchKey = watchService.take()) != null) {
                watchKey.pollEvents().forEach(watchEvent -> {
                    Object context = watchEvent.context();
                    if (context instanceof Path) {
                        Path path = ((Path) context);
                        File file = path.toFile();
                        if (file.getName().equals(applicationProperties.getItemFileName())) {
                            try {
                                List<String> items = itemParser.parseItems(applicationProperties.getItemFileDirectory() + file.getName());
                                items
                                        .stream()
                                        .filter(item -> {
                                            for (String filter : filters) {
                                                if (item.contains(filter)) {
                                                    log.debug(String.format("No notification sent for item '%s'  was filtered since filter '%s' applies", item, filter));
                                                    return false;
                                                }
                                            }
                                            return true;
                                        })
                                        .forEach(item -> {
                                            log.info(String.format("New item: '%s'", item));
                                            discordNotifier.send(item);
                                        });
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
                watchKey.reset();
            }


        } finally {
            if (watchService != null) {
                watchService.close();
            }
        }
    }

}
