package net.joe.itemfindnotifier.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebhookRequest {

    String content;
}
