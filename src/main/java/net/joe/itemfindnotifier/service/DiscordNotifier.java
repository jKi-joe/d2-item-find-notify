package net.joe.itemfindnotifier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.joe.itemfindnotifier.config.ApplicationProperties;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Log4j2
public class DiscordNotifier {

    public static final String LINE_BREAK = "\\n";
    private final OkHttpClient client;

    private final ApplicationProperties applicationProperties;

    public void send(final String message) {
        try {
            String randomPrefix = applicationProperties.getMessagePrefixes().get(new Random().nextInt(applicationProperties.getMessagePrefixes().size()));
            String suffix = applicationProperties.getMessageSuffix();
            String newItemMessage = String.format("%s%s%s%s%s", randomPrefix, LINE_BREAK, message, LINE_BREAK, suffix);
            newItemMessage = newItemMessage.replaceAll("\\|", "\\\\n");
            Response execute = client.newCall(new Request.Builder()
                    .url(applicationProperties.getDiscordWebhookUrl())
                    .post(RequestBody.create(MediaType.parse("application/json"), String.valueOf("{\"content\": \"" + newItemMessage + "\"}")))
                    .build()).execute();
            if (execute.code() != 204) {
                log.error(String.format("Could not send to discord since response on webhook was '%s'", execute.code()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
