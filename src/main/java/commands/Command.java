package commands;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

/**
 * Created by Thomas on 28.01.2018.
 */
public interface Command {

    void runCommand(MessageReceivedEvent event, List<String> args);
}
