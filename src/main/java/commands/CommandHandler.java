package commands;

import objects.Globals;
import objects.PostfixNotation;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.*;


public class CommandHandler implements IListener<MessageReceivedEvent> {

    private static Map<String, Command> commandMap = new HashMap<String, Command>() {{
        put("about", (event, args) -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.withAuthorName("chili_nor");
            builder.withTitle("Commands");
            builder.appendField("testcommand", "prints out what you said as arguments", true);
            builder.appendField("calculate", "evaluates an expression, cant have whitespaces but can have trigonometric functions and parenthesises. supports the following: +-/*% tan, cos, sin, atan, asin, acos. Slap true of you want radians on the end or false", true);
            builder.appendField("rtop", "Specify a subreddit after the command, gives you the top item off of that subreddit, based on the last 24 hours.", true);
            RequestBuffer.request(() -> event.getChannel().sendMessage(builder.build()));
        });

        put("calculate", (event, args) -> {
            boolean isRadian = false;
            if (args.size() > 2) isRadian = args.get(2).equals("true");
            try {
                Globals.sendMessage(event.getChannel(), "solution: " + PostfixNotation.evaluateExpression(args.get(1), isRadian));
            } catch (Exception e) {
                Globals.sendMessage(event.getChannel(), e.getMessage());
            }
        });

        put("howto", (event, args) -> {
            args.remove(0);
            String query = String.join(" ", args);
            Globals.sendMessage(event.getChannel(),"probably going to do some stack overflow magic here, also here is your query: " + query );

        });

        put("rtop", (event, args) -> {
            args.remove(0);
            String requestedReddit = args.get(0);
            RedditCommands.redditTop(requestedReddit, event);
        });

        put("rnew",((event, args) -> {
            args.remove(0);
            String requestedReddit = args.get(0);
            RedditCommands.redditNew(requestedReddit,event);
        }));

        put("next", ((event, args) -> {
            String username = event.getAuthor().getName();
            RedditCommands.next(username, event);
        }));

    }};

    @Override
    public void handle(MessageReceivedEvent event) {
        String[] arguments = event.getMessage().getContent().split(" ");
        if (arguments[0].startsWith(Globals.PREFIX) && arguments.length > 1) {
            List<String> commands = new ArrayList<>(Arrays.asList(arguments));
            commands.remove(0);
            if (commandMap.containsKey(commands.get(0))) {
                commandMap.get(commands.get(0)).runCommand(event, commands);
            }
        } else if (arguments[0].startsWith(Globals.PREFIX)) {
            Globals.sendMessage(event.getChannel(), "What do you want?");
        }
    }


}
