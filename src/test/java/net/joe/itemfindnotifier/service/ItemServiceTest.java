package net.joe.itemfindnotifier.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Strings;

import net.joe.itemfindnotifier.config.ApplicationConfiguration;
import net.joe.itemfindnotifier.data.Item;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RunWith(SpringRunner.class)
@Import(ApplicationConfiguration.class)
@DataJpaTest
@Ignore
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Test
    public void create() {
        Item item = Item.builder()
                .name("(unique) Magefist (75) | Light Gauntlets | Defense: 25 | Durability: 11 of 18 | Required Strength: 45 | Required Level: 23 | +1 to Fire Skills | +20% Faster Cast Rate | Adds 1-6 fire damage | +25% Enhanced Defense | +10 Defense | Regenerate Mana 25%")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 25, 18))))
                .build();
        Item created = itemService.create(item);
        assertFalse(Strings.isNullOrEmpty(created.getId()));
        assertEq(item, created);
    }

    @Test
    public void findById() {
        Item mage1 = Item.builder()
                .name("(unique) Magefist (75) | Light Gauntlets | Defense: 25 | Durability: 11 of 18 | Required Strength: 45 | Required Level: 23 | +1 to Fire Skills | +20% Faster Cast Rate | Adds 1-6 fire damage | +25% Enhanced Defense | +10 Defense | Regenerate Mana 25%")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 25, 18))))
                .build();
        Item fRuby = Item.builder()
                .name("(normal) Flawless Ruby (1) | Can be Inserted into Socketed Items |  | Weapons: Adds 10-16 fire damage | Armor: +31 to Life | Helms: +31 to Life | Shields: Fire Resist +28% |  | Required Level: 15 {Cubing 2}")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30, 45))))
                .build();
        itemService.create(fRuby);
        Item created = itemService.create(mage1);
        assertFalse(Strings.isNullOrEmpty(created.getId()));
        assertEq(mage1, created);

        Item foundItem = itemService.findItemById(created.getId());
        assertEquals(created.getId(), foundItem.getId());
        assertEq(foundItem, mage1);
    }

    @Test
    public void findByNameAndTimestamp() {
        Item mage1 = Item.builder()
                .name("(unique) Magefist (75) | Light Gauntlets | Defense: 25 | Durability: 11 of 18 | Required Strength: 45 | Required Level: 23 | +1 to Fire Skills | +20% Faster Cast Rate | Adds 1-6 fire damage | +25% Enhanced Defense | +10 Defense | Regenerate Mana 25%")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 25, 18))))
                .build();
        Item fRuby = Item.builder()
                .name("(normal) Flawless Ruby (1) | Can be Inserted into Socketed Items |  | Weapons: Adds 10-16 fire damage | Armor: +31 to Life | Helms: +31 to Life | Shields: Fire Resist +28% |  | Required Level: 15 {Cubing 2}")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30, 45))))
                .build();
        Item mage2 = Item.builder()
                .name("(unique) Magefist (75) | Light Gauntlets | Defense: 25 | Durability: 11 of 18 | Required Strength: 45 | Required Level: 23 | +1 to Fire Skills | +20% Faster Cast Rate | Adds 1-6 fire damage | +25% Enhanced Defense | +10 Defense | Regenerate Mana 25%")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 25, 17))))
                .build();
        itemService.create(fRuby);
        itemService.create(mage2);
        Item created = itemService.create(mage1);
        assertFalse(Strings.isNullOrEmpty(created.getId()));

        Item foundItem = itemService.findItemByTimestamp(mage1.getTimestamp());
        assertEq(foundItem, mage1);
        assertEquals(created.getId(), foundItem.getId());
    }

    @Test
    public void itemsPresent() {
        assertTrue(itemService.dbEmpty());
        itemService.create(Item.builder().name("someItem").timestamp(Timestamp.from(Instant.now())).build());
        assertFalse(itemService.dbEmpty());
        itemService.create(Item.builder().name("someItem").timestamp(Timestamp.from(Instant.now())).build());
        assertFalse(itemService.dbEmpty());
    }

    private void assertEq(final Item item, final Item created) {
        assertEquals(item.getName(), created.getName());
        assertEquals(item.getTimestamp(), created.getTimestamp());
    }
}
