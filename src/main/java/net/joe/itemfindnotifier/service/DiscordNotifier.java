package net.joe.itemfindnotifier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import net.joe.itemfindnotifier.config.ApplicationProperties;

import org.springframework.stereotype.Service;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
@RequiredArgsConstructor
@Log4j2
public class DiscordNotifier {

    private final OkHttpClient client;

    private final ApplicationProperties applicationProperties;

    public void send(final String newItemMessage) {
        try {
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
