package objects;

import objects.reddit.subtypes.SubData;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class Globals {
    public static final String PREFIX = "wat";
    public static final String TOKEN = "PUT TOKEN HERE";


    public static void sendMessage(IChannel channel, String message) {
        RequestBuffer.request(() -> {
            try {
                channel.sendMessage(message);
            } catch (DiscordException err){
                System.out.println("Error sending message : " + err.getErrorMessage());
            }
        });
    }

    public static void buildRedditEmbeddedContent(IChannel channel, SubData data){
        boolean isImage = data.url.matches(".*\\.(gif|jpg|jpeg|tiff|png)");
            if(isImage){
                EmbedBuilder builder = new EmbedBuilder();
                builder.withColor(255, 0, 0);
                builder.withAuthorName(data.title);
                builder.withFooterText("Published by: " + data.author);
                builder.withImage(data.url);
                builder.withUrl(data.url);
                RequestBuffer.request(() -> channel.sendMessage(builder.build()));
            }else{
                Globals.sendMessage(channel,data.url);
            }
    }
}
