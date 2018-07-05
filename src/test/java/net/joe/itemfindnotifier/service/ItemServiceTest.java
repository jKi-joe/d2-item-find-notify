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
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RunWith(SpringRunner.class)
@Import(ApplicationConfiguration.class)
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Test
    public void create() {
        Item item = Item.builder()
                .foundBy("Soso 1")
                .name("(unique) Magefist (75) | Light Gauntlets")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 25, 18))))
                .build();
        Item created = itemService.create(item);
        assertFalse(Strings.isNullOrEmpty(created.getId()));
        assertEq(item, created);
    }

    @Test
    public void findById() {
        Item mage1 = Item.builder()
                .foundBy("Soso 1")
                .name("(unique) Magefist (75) | Light Gauntlets")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 25, 18))))
                .build();
        Item fRuby = Item.builder()
                .foundBy("Soso 2")
                .name("(normal) Flawless Ruby (1) | Can be Inserted into Socketed Items")
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
    public void findByTimestampAndFoundBy() {
        Item mage1 = Item.builder()
                .foundBy("Soso 1")
                .name("(unique) Magefist (75) | Light Gauntlets")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 25, 18))))
                .build();
        Item fRuby = Item.builder()
                .foundBy("Soso 2")
                .name("(normal) Flawless Ruby (1) | Can be Inserted into Socketed Items")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30, 45))))
                .build();
        Item mage2 = Item.builder()
                .name("(unique) Magefist (75) | Light Gauntlets")
                .foundBy("Soso 1")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 25, 19))))
                .build();
        Item mage3 = Item.builder()
                .name("(unique) Magefist (75) | Light Gauntlets")
                .foundBy("Soso 2")
                .timestamp(Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 25, 19))))
                .build();

        itemService.create(fRuby);
        itemService.create(mage1);
        Item createdMage2 = itemService.create(mage2);
        Item createdMage3 = itemService.create(mage3);

        Item foundItem1 = itemService.findItemByTimestampAndFoundBy(mage2.getTimestamp(), mage2.getFoundBy());
        assertEq(foundItem1, createdMage2);
        assertEquals(createdMage2.getId(), foundItem1.getId());

        Item foundItem2 = itemService.findItemByTimestampAndFoundBy(mage3.getTimestamp(), mage3.getFoundBy());
        assertEq(foundItem2, createdMage3);
        assertEquals(createdMage3.getId(), foundItem2.getId());
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
        assertEquals(item.getTimestamp(), created.getTimestamp());
        assertEquals(item.getFoundBy(), created.getFoundBy());
    }
}
