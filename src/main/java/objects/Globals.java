package objects;

import discord4j.core.event.domain.message.MessageCreateEvent;
import objects.reddit.subtypes.SubData;

import java.awt.*;

public class Globals {
    public static final String PREFIX = "wat";


    public static void sendMessage(MessageCreateEvent event, String message) {
        event.getMessage().getChannel().block().createMessage(message).block();
    }

    public static void buildRedditEmbeddedContent(MessageCreateEvent event, SubData data) {
        boolean isImage = data.url.matches(".*\\.(gif|jpg|jpeg|tiff|png)");

        if (isImage) {
            event.getMessage().getChannel().block().createEmbed(spec -> {
                spec.setColor(new Color(255, 0, 0));
                spec.setAuthor(data.title,null,null);
                spec.setFooter("Published by: " + data.author,null);
                spec.setImage(data.url);
                spec.setUrl(data.url);
            }).block();
        } else {
            Globals.sendMessage(event, data.url);
        }
    }
}
