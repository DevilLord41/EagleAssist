package org.redeagle.command.core;

import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.Command;

public class Ping extends Command {

	@Override
	public String getName() {
		return "ping";
	}

	@Override
	public String getDescription() {
		return "Check ping...";
	}

	@Override
	public List<String> getAliases() {
		return super.getAliases();
	}
	

	@Override
	public List<String> getParameter() {
		return super.getParameter();
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m,
			User u, Guild g, String... args) {
		e.getChannel().sendMessage("Pong ~ **" + (int)bot.getPing() + "**ms").queue();
	}

}
