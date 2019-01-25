package org.redeagle.command;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.MessageType;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.BotConfiguration;

public class CommandHandler {
	
	private List<Command> registeredCommands = new ArrayList<>();
	
	public void  register(Command cmd) {
		registeredCommands.add(cmd);
	}

	public List<Command> getRegisteredCommand() {
		return registeredCommands;
	}
	
	public void handle(GuildMessageReceivedEvent e, JDA bot) {
		if(e.getMessage().getType() != MessageType.DEFAULT || e.getAuthor().isBot()) return;
		String[] args = e.getMessage().getContentRaw().split(" ");
		registeredCommands.forEach(cmd -> {
			List<String> aliases = new ArrayList<>(cmd.getAliases());
			aliases.add(cmd.getName());
			aliases.forEach(a -> {
				if((args[0].replace(BotConfiguration.BOT_PREFIX, "")).equals(a)) {
					if(cmd.ownerOnly() && !e.getAuthor().getId().equals("239627873701330944")) return;
					else cmd.onExecute(bot, e, e.getMessage(), e.getMessage().getContentRaw(), e.getAuthor(), e.getGuild(), args);
				}
			});
		});
		
	}
}
