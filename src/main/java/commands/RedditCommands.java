package commands;

import networking.NetworkApi;
import objects.Globals;
import objects.reddit.RedditResponse;
import objects.reddit.subtypes.Child;
import objects.reddit.subtypes.SubData;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RedditCommands {
    static HashMap<String, RedditResponse> redditMap = new HashMap<>();
    static HashMap<String, Map.Entry<String, Integer>> userState = new HashMap<>();

    public static void redditTop(String requestedReddit, MessageReceivedEvent event) {
        String username = event.getAuthor().getName();
        IChannel channel = event.getChannel();
        if (!redditMap.containsKey(requestedReddit)) {
            CompletableFuture<RedditResponse> response = NetworkApi.getTopPost(requestedReddit);
            pushResult(response,
                    channel,
                    requestedReddit,
                    username
            );
        } else {
            Globals.sendMessage(channel, "You are already looking at it, type next");
        }
    }

    private static void pushResult(CompletableFuture<RedditResponse> response,IChannel channel,String requestedReddit,String username) {
        response.whenComplete((res, t) -> {
            if (t != null) {
                Globals.sendMessage(channel, "Something went wrong");
                return;
            } else {
                redditMap.put(requestedReddit, res);
            }
            userState.put(username, new AbstractMap.SimpleEntry<>(requestedReddit, 0));

            Child child = redditMap.get(requestedReddit).data.children.get(0);
            publishContent(child.data,channel);
        });
    }


    public static void next(String username, MessageReceivedEvent event) {
        IChannel channel = event.getChannel();
        if (userState.containsKey(username)) {
            Map.Entry<String, Integer> entry = userState.get(username);
            RedditResponse reddit = redditMap.get(entry.getKey());
            if (entry.getValue() == reddit.data.children.size()) {
                Globals.sendMessage(channel, "I'm out of posts! Too much of the good stuff is not good for you!");
                return;
            }
            entry.setValue(entry.getValue() + 1);
            SubData data = reddit.data.children.get(entry.getValue()).data;
            userState.put(username, entry);
            publishContent(data,channel);

        } else {
            Globals.sendMessage(channel, "you need to specify which reddit you want to look at!");
        }
    }

    public static void publishContent(SubData data, IChannel channel){
        if (!channel.isNSFW() && data.over_18) {
            Globals.sendMessage(channel, "itsa nsfw cant post that here.");
        } else {
            Globals.buildRedditEmbeddedContent(channel, data);
        }
    }

    public static void redditNew(String requestedReddit, MessageReceivedEvent event) {
        String username = event.getAuthor().getName();
        IChannel channel = event.getChannel();
        if(!redditMap.containsKey(requestedReddit)){
            pushResult(NetworkApi.getNewPost(requestedReddit),
                    channel,
                    requestedReddit,
                    username
            );
        }else{
            Globals.sendMessage(channel, "You are already looking at it, type next");
        }
    }
}



