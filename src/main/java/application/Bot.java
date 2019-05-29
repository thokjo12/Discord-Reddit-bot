package application;

import commands.CommandHandler;
import objects.ClientLib;
import objects.Globals;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;


public class
Bot{
    public static void main(String[] args) {
        IDiscordClient client = ClientLib.createClient(Globals.TOKEN, true);
        EventDispatcher dispatcher = client.getDispatcher();
        dispatcher.registerListener(new CommandHandler());
    }
}
