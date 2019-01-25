package org.redeagle.command;

import java.util.Collections;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class Command {

	public String getName() {
		return null;
	}
	
	public String getDescription() {
		return "Not Available";
	}
	
	public List<String> getParameter() {
		return Collections.emptyList();
	}
	
	public List<String> getAliases() {
		return Collections.emptyList();
	}
	
	public boolean ownerOnly() {
		return false;
	}
	
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
		
	}
}
