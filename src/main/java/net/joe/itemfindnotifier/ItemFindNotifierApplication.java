package net.joe.itemfindnotifier;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import net.joe.itemfindnotifier.config.ApplicationConfiguration;
import net.joe.itemfindnotifier.config.ApplicationProperties;
import net.joe.itemfindnotifier.data.Item;
import net.joe.itemfindnotifier.service.DbPopulator;
import net.joe.itemfindnotifier.service.ItemParser;
import net.joe.itemfindnotifier.service.ItemService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SpringBootApplication
@Import(ApplicationConfiguration.class)
@RequiredArgsConstructor
@Log4j2
public class ItemFindNotifierApplication implements CommandLineRunner {

    private final ApplicationProperties applicationProperties;

    private final ItemParser itemParser;

    private final ItemService itemService;

    private final OkHttpClient client = new OkHttpClient();

    private final DbPopulator dbPopulator;


    public static void main(String[] args) {
        SpringApplication.run(ItemFindNotifierApplication.class, args);
    }

    @Override
    public void run(final String... args) throws IOException, InterruptedException {
        dbPopulator.populate();

        WatchService watchService = null;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path folderToWatch = Paths.get(applicationProperties.getItemFileDirectory());
            folderToWatch.register(watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            WatchKey watchKey;
            List<String> filters = applicationProperties.getFilters();
            log.info("ItemFindNotify has started successfully");
            log.info(String.format("Watching directory '%s' for file '%s' now", applicationProperties.getItemFileDirectory(), applicationProperties.getItemFileName()));
            while ((watchKey = watchService.take()) != null) {
                watchKey.pollEvents().forEach(watchEvent -> {
                    Object context = watchEvent.context();
                    if (context instanceof Path) {
                        Path path = ((Path) context);
                        File file = path.toFile();
                        if (file.getName().equals(applicationProperties.getItemFileName())) {
                            try {
                                List<Item> items = itemParser.parseItems(applicationProperties.getItemFileDirectory() + file.getName());
                                items
                                        .stream()
                                        .filter(item -> {
                                            if (itemService.findItemByTimestampAndFoundBy(item.getTimestamp(), item.getFoundBy()) == null) {
                                                return true;
                                            } else {
                                                log.debug(String.format("No notification sent for item '%s' found by '%s' at '%s' sent, since it is already in DB", item.getName(), item.getFoundBy(), item.getTimestamp()));
                                                return false;
                                            }

                                        })
                                        .filter(item -> {
                                            for (String filter : filters) {
                                                if (item.getName().contains(filter)) {
                                                    log.debug(String.format("No notification sent for item '%s'  was filtered since filter '%s' applies", item.getName(), filter));
                                                    return false;
                                                }
                                            }
                                            return true;
                                        })
                                        .forEach(item -> {
                                            log.info(String.format("New item: '%s' found at '%s'", item.getName(), item.getTimestamp()));
                                            itemService.create(item);

                                            String randomPrefix = applicationProperties.getMessagePrefixes().get(new Random().nextInt(applicationProperties.getMessagePrefixes().size()));
                                            String newItemMessage = String.format("%s%s%s", randomPrefix, "\\n", item.getName());
                                            newItemMessage = newItemMessage.replaceAll("\\|", "\\\\n");
                                            try {
                                                Response execute = client.newCall(new Request.Builder()
                                                        .url(applicationProperties.getDiscordWebhookUrl())
                                                        .post(RequestBody.create(MediaType.parse("application/json"), String.valueOf("{\"content\": \"" + newItemMessage + "\"}")))
                                                        .build()).execute();
                                                if (execute.code() != 204) {
                                                    log.error(String.format("Could not send to discord since response on webhook was '%s'", execute.code()));
                                                }
                                            } catch (IOException e) {
                                                throw new RuntimeException(e);
                                            }
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
