package net.joe.itemfindnotifier.service;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Log4j2
@Scope("singleton")
public class ItemParser {

    private AtomicLong lastFileReadUntil = new AtomicLong(-1);

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
        lastFileReadUntil.set(totalLines);
    }

    List<String> parseItems(String itemFilePath) throws IOException {
        log.debug(String.format("Reading file from path '%s'", itemFilePath));
        List<String> newItems = Lists.newArrayList();
        LineIterator lineIterator = initFileIterator(itemFilePath);
        try {
            long linesRead = 0;
            while (lineIterator.hasNext()) {
                String itemLine = lineIterator.nextLine();
                if (lastFileReadUntil.get() != -1 && linesRead >= lastFileReadUntil.get()) {
                    if (itemLine.matches(".*<.*Kept>.*")) {
                        String e = itemLine.replaceAll(".*<.*Kept> ", "");
                        newItems.add(e);
                    }
                }
                linesRead++;
            }
            // If nothing is read, then prevent starting over
            // This might happen if during a file writing a file is flushed first,
            // then nothing will be read
            if (linesRead != 0) {
                lastFileReadUntil.set(linesRead);
            }
        } finally {
            LineIterator.closeQuietly(lineIterator);
        }
        return newItems;
    }

    private LineIterator initFileIterator(String itemFilePath) throws IOException {
        return FileUtils.lineIterator(new File(itemFilePath));
    }

}
