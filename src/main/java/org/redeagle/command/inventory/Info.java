package org.redeagle.command.inventory;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.Command;

public class Info extends Command {

	@Override
	public String getName() {
		return "info";
	}

	@Override
	public String getDescription() {
		return "Melihat informasi item";
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("<itemID>");
	}

	@Override
	public List<String> getAliases() {
		return super.getAliases();
	}

	@Override
	public boolean ownerOnly() {
		return super.ownerOnly();
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
		super.onExecute(bot, e, msg, m, u, g, args);
	}

}
