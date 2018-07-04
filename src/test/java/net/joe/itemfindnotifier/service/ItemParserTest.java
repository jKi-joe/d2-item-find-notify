package net.joe.itemfindnotifier.service;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;

import net.joe.itemfindnotifier.config.ApplicationConfiguration;
import net.joe.itemfindnotifier.data.Item;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RunWith(SpringRunner.class)
@Import(ApplicationConfiguration.class)
public class ItemParserTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ItemParser itemParser;

    @Test
    public void readItem() throws IOException {
        List<Item> items = itemParser.parseItems(resourceLoader.getResource("ItemLog.txt").getURI().getPath());
        assertFalse(items.isEmpty());
        Item magefist = Item.builder()
                .name("(unique) Magefist (75) | Light Gauntlets | Defense: 25 | Durability: 11 of 18 | Required Strength: 45 | Required Level: 23 | +1 to Fire Skills | +20% Faster Cast Rate | Adds 1-6 fire damage | +25% Enhanced Defense | +10 Defense | Regenerate Mana 25%")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 25, 18))))
                .build();
        Item fRuby = Item.builder()
                .name("(normal) Flawless Ruby (1) | Can be Inserted into Socketed Items |  | Weapons: Adds 10-16 fire damage | Armor: +31 to Life | Helms: +31 to Life | Shields: Fire Resist +28% |  | Required Level: 15 {Cubing 2}")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30, 45))))
                .build();
        assertThat(items, containsInAnyOrder(magefist, fRuby));
    }
}
