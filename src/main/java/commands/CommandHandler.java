package commands;

import discord4j.core.event.domain.message.MessageCreateEvent;
import objects.Globals;
import objects.PostfixNotation;
import objects.TextData;

import java.util.*;

public class CommandHandler{
    private static Map<String, Command> commandMap = new HashMap<String, Command>() {{
        put("about", (event, args) -> {
            event.getMessage().getChannel().block().createEmbed(spec ->{
                spec.setAuthor("chili_nor",null,null);
                spec.setTitle("Commands");
                spec.addField("testcommand","prints out what you said as arguments", true);
                spec.addField("calculate", "evaluates an expression, cant have whitespaces but can have trigonometric functions and parenthesises. supports the following: +-/*% tan, cos, sin, atan, asin, acos. Slap true of you want radians on the end or false", true);
                spec.addField("rtop", "Specify a subreddit after the command, gives you the top item off of that subreddit, based on the last 24 hours.", true);
            }).block();
        });

        put("calculate", (event, args) -> {
            boolean isRadian = false;
            if (args.size() > 2) isRadian = args.get(2).equals("true");
            try {
                Globals.sendMessage(event, "solution: " + PostfixNotation.evaluateExpression(args.get(1), isRadian));
            } catch (Exception e) {
                Globals.sendMessage(event, e.getMessage());
            }
        });

        put("howto", (event, args) -> {
            args.remove(0);
            String query = String.join(" ", args);
            Globals.sendMessage(event,TextData.howto_text + query );

        });

        put("rtop", (event, args) -> {
            if(args.size() == 1 || args.size() > 2){
                Globals.sendMessage(event,TextData.rtop_text_error);
            }
            args.remove(0);
            String requestedReddit = args.get(0);
            RedditCommands.redditTop(requestedReddit, event);
        });

        put("rnew",(event, args) -> {
            if(args.size() == 1 || args.size() > 2){
                Globals.sendMessage(event,TextData.rnew_text_error);
            }
            args.remove(0);
            String requestedReddit = args.get(0);
            RedditCommands.redditNew(requestedReddit,event);
        });

        put("next", (event, args) -> {
            event.getMember().ifPresent(user -> RedditCommands.next(event));
        });
    }};

    public void handle(MessageCreateEvent event) {
        event.getMessage().getContent().ifPresent(item -> {
            String[] arguments = item.split(" ");
            if (arguments[0].startsWith(Globals.PREFIX) && arguments.length > 1) {
                List<String> commands = new ArrayList<>(Arrays.asList(arguments));
                commands.remove(0);
                if (commandMap.containsKey(commands.get(0))) {
                    commandMap.get(commands.get(0)).runCommand(event, commands);
                }
            } else if (arguments[0].startsWith(Globals.PREFIX)) {
                Globals.sendMessage(event, "What do you want?");
            }
        });
    }
}
