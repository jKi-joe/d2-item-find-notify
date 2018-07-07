package net.joe.itemfindnotifier.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ItemParser {

    private long lastFileReadUntil = -1;

    public void initLastFileReadUntil(String itemFilePath) throws IOException {
        LineIterator lineIterator = initFileIterator(itemFilePath);
        long totalLines = 0;
        try {
            while (lineIterator.hasNext()) {
                lineIterator.nextLine();
                totalLines++;
            }
        } finally {
            LineIterator.closeQuietly(lineIterator);
        }
        lastFileReadUntil = totalLines;
    }

    List<String> parseItems(String itemFilePath) throws IOException {
        log.debug(String.format("Reading file from path '%s'", itemFilePath));
        List<String> newItems = Lists.newArrayList();
        LineIterator lineIterator = initFileIterator(itemFilePath);
        long linesRead = 0;
        try {
            while (lineIterator.hasNext()) {
                String itemLine = lineIterator.nextLine();
                if (lastFileReadUntil != -1 && linesRead >= lastFileReadUntil) {
                    if (itemLine.matches(".*<Kept>.*")) {
                        newItems.add(itemLine.replaceAll(".*<Kept> ", ""));
                    }
                }
                linesRead++;
            }
            lastFileReadUntil = linesRead;
        } finally {
            LineIterator.closeQuietly(lineIterator);
        }
        return newItems;
    }

    private LineIterator initFileIterator(String itemFilePath) throws IOException {
        return FileUtils.lineIterator(new File(itemFilePath));
    }

}
