package commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.data.stored.ChannelBean;
import discord4j.core.object.entity.Channel;

import networking.NetworkApi;
import objects.Globals;
import objects.reddit.RedditResponse;
import objects.reddit.subtypes.Child;
import objects.reddit.subtypes.SubData;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RedditCommands {
    static HashMap<String, RedditResponse> redditMap = new HashMap<>();
    static HashMap<String, Map.Entry<String, Integer>> userState = new HashMap<>();

    public static void redditTop(String requestedReddit, MessageCreateEvent event) {

        if (!redditMap.containsKey(requestedReddit)) {
            pushResult(NetworkApi.getTopPost(requestedReddit),
                    event,
                    requestedReddit
            );
        } else {
            Globals.sendMessage(event, "You are already looking at it, type next");
        }
    }

    private static void pushResult(CompletableFuture<RedditResponse> response, MessageCreateEvent event, String requestedReddit) {
        response.whenComplete((res, t) -> {
            if (t != null) {
                Globals.sendMessage(event, "Something went wrong");
                return;
            } else {
                redditMap.put(requestedReddit, res);
            }
            String username = event.getMessage().getAuthor().get().getUsername();
            Map.Entry<String,Integer> prevEntry = userState.put(username, new AbstractMap.SimpleEntry<String,Integer>(requestedReddit, 0));
            redditMap.remove(prevEntry.getKey());
            Child child = redditMap.get(requestedReddit).data.children.get(0);
            publishContent(child.data,event);
        });
    }


    public static void next( MessageCreateEvent event) {
        String username = event.getMessage().getAuthor().get().getUsername();
        if (userState.containsKey(username)) {
            Map.Entry<String, Integer> entry = userState.get(username);
            RedditResponse reddit = redditMap.get(entry.getKey());
            if (entry.getValue() == reddit.data.children.size()) {
                Globals.sendMessage(event, "I'm out of posts! Too much of the good stuff is not good for you!");
                return;
            }
            entry.setValue(entry.getValue() + 1);
            SubData data = reddit.data.children.get(entry.getValue()).data;
            userState.put(username, entry);
            publishContent(data,event);

        } else {
            Globals.sendMessage(event, "you need to specify which reddit you want to look at!");
        }
    }

    public static void publishContent(SubData data, MessageCreateEvent event){
        ChannelBean channelMeta = new ChannelBean(event.getMessage().getChannelId().asLong(), Channel.Type.DM.getValue());

        if (!channelMeta.isNsfw() && data.over_18) {
            Globals.sendMessage(event, "itsa nsfw cant post that here.");
        } else {
            Globals.buildRedditEmbeddedContent(event, data);
        }
    }

    public static void redditNew(String requestedReddit, MessageCreateEvent event) {
        if(!redditMap.containsKey(requestedReddit)){
            pushResult(NetworkApi.getNewPost(requestedReddit),
                    event,
                    requestedReddit
            );
        }else{
            Globals.sendMessage(event, "You are already looking at it, type next");
        }
    }
}



