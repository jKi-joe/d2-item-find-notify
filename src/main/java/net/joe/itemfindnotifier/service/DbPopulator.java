package net.joe.itemfindnotifier.service;

import lombok.RequiredArgsConstructor;

import net.joe.itemfindnotifier.config.ApplicationProperties;
import net.joe.itemfindnotifier.data.Item;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DbPopulator {

    private final ItemParser itemParser;

    private final ItemService itemService;

    private final ApplicationProperties applicationProperties;

    public void populate() throws IOException {
        if (itemService.dbEmpty()) {
            itemParser.parseItems(applicationProperties.getItemFileDirectory() + applicationProperties.getItemFileName())
                    .forEach(itemService::create);
        }
    }

}
