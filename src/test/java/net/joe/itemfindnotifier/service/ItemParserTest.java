package net.joe.itemfindnotifier.service;

import net.joe.itemfindnotifier.config.ApplicationConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Import(ApplicationConfiguration.class)
public class ItemParserTest {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private ItemParser itemParser;

    @Test
    public void readItem() throws IOException {
        itemParser.initLastFileReadUntil(resourceLoader.getResource("EmptyFile.txt").getURI().getPath());
        List<String> items = itemParser.parseItems(resourceLoader.getResource("ItemLog.txt").getURI().getPath());
        assertFalse(items.isEmpty());
        assertEquals(4, items.size());
        assertThat(items, containsInAnyOrder(
                "(unique) Magefist (75) | Light Gauntlets | Defense: 25 | Durability: 11 of 18 | Required Strength: 45 | Required Level: 23 | +1 to Fire Skills | +20% Faster Cast Rate | Adds 1-6 fire damage | +25% Enhanced Defense | +10 Defense | Regenerate Mana 25%",
                "(unique) Magefist (75) | Light Gauntlets | Defense: 25 | Durability: 11 of 18 | Required Strength: 45 | Required Level: 23 | +1 to Fire Skills | +20% Faster Cast Rate | Adds 1-6 fire damage | +25% Enhanced Defense | +10 Defense | Regenerate Mana 25%",
                "(normal) Flawless Ruby (1) | Can be Inserted into Socketed Items |  | Weapons: Adds 10-16 fire damage | Armor: +31 to Life | Helms: +31 to Life | Shields: Fire Resist +28% |  | Required Level: 15 {Cubing 2}",
                "(normal) Perfect Ruby (1) | Can be Inserted into Socketed Items |  | Weapons: Adds 15-20 fire damage | Armor: +38 to Life | Helms: +38 to Life | Shields: Fire Resist +40% |  | Required Level: 18"));
    }
}
