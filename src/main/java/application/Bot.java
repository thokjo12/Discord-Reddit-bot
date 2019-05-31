package application;

import commands.CommandHandler;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.domain.message.MessageCreateEvent;


public class Bot{
    public static void main(String[] args) {
        if (args.length == 0){
            System.out.println("you need to set a token as an argument!");
        }
        DiscordClient client = new DiscordClientBuilder(args[0]).build();
        CommandHandler handler = new CommandHandler();
        client.getEventDispatcher().on(MessageCreateEvent.class).subscribe(handler::handle);
        client.login().block();

    }
}