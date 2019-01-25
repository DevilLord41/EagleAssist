package org.redeagle.command.core;

import java.io.File;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.BotConfiguration;
import org.redeagle.command.Command;

public class Logo extends Command {

	@Override
	public String getName() {
		return "logo";
	}

	@Override
	public String getDescription() {
		return "Logo RE";
	}

	@Override
	public List<String> getParameter() {
		return super.getParameter();
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
		msg.getTextChannel().sendFile(new File(BotConfiguration.PATH + "/asset/logo.jpg"), "Here is your order sir..").queue();
	}

}
