package commands;


import discord4j.core.event.domain.message.MessageCreateEvent;

import java.util.List;

/**
 * Created by Thomas on 28.01.2018.
 */
public interface Command {

    void runCommand(MessageCreateEvent event, List<String> args);
}
