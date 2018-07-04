package net.joe.itemfindnotifier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import net.joe.itemfindnotifier.config.ApplicationProperties;
import net.joe.itemfindnotifier.data.Item;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class ItemFileListener {

    private final ItemParser itemParser;

    private final ItemService itemService;

    private final ApplicationProperties applicationProperties;

//    @Override
//    public void onChange(final Set<ChangedFiles> changeSet) {
//        changeSet.forEach(changedFiles -> changedFiles.getFiles().forEach(changedFile -> {
//            if (changedFile.getFile() != null && changedFile.getFile().getName().equals(applicationProperties.getItemFileName())) {
//                try {
//                    List<Item> items = itemParser.parseItems(changedFile.getFile().getPath());
//                    items.forEach(item -> log.debug(String.format("New item '%s' from '%s'", item.getName(), item.getTimestamp())));
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }));
//    }

}
