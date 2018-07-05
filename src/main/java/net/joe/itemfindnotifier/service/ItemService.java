package net.joe.itemfindnotifier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import net.joe.itemfindnotifier.data.Item;
import net.joe.itemfindnotifier.persistence.ItemRepository;
import net.joe.itemfindnotifier.persistence.data.ItemDto;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item create(Item item) {
        return convert(itemRepository.save(convert(item)));
    }

    public Item findItemById(String id) {
        return itemRepository.findById(id).map(this::convert).orElse(null);
    }

    public boolean dbEmpty() {
        return itemRepository.findAll().isEmpty();
    }

    public Item findItemByTimestampAndFoundBy(Timestamp timestamp, String foundBy) {
        List<ItemDto> itemsByTimestampAndFoundBy = itemRepository.findAllByTimestampAndFoundBy(timestamp, foundBy);
        return itemsByTimestampAndFoundBy.isEmpty() ? null : convert(itemsByTimestampAndFoundBy.get(0));
    }

    private ItemDto convert(final Item item) {
        return ItemDto.builder()
                .id(UUID.randomUUID().toString())
                .foundBy(item.getFoundBy())
                .timestamp(item.getTimestamp())
                .build();
    }

    private Item convert(final ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .foundBy(itemDto.getFoundBy())
                .timestamp(itemDto.getTimestamp())
                .build();
    }

}
