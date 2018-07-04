package net.joe.itemfindnotifier.service;

import com.google.common.collect.Lists;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import net.joe.itemfindnotifier.data.Item;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class ItemParser  {

    public List<Item> parseItems(String itemFilePath) throws IOException {
        List<Item> items = Lists.newArrayList();
        Stream<String> lines = Files.lines(Paths.get(itemFilePath));
        log.debug(String.format("Reading file from path '%s'", itemFilePath));
        lines.forEach(itemLine -> {
            if (itemLine.matches(".*<Kept>.*")) {
                items.add(Item.builder()
                        .name(itemLine.replaceAll(".*<Kept> ", ""))
                        .timestamp(parseTimeFromToday(itemLine))
                        .build());
            }
        });
        lines.close();
        return items;
    }

    private Timestamp parseTimeFromToday(final String s) {
        String date = s.replaceFirst("\\[", "").replaceFirst("\\].*", "");
        LocalTime localTime = LocalTime.parse(date, DateTimeFormatter.ofPattern("HH:mm:ss"));
        return Timestamp.valueOf(LocalDateTime.of(LocalDate.now(), localTime));
    }
}
