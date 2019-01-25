package org.redeagle.command.owner;

import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.Command;

public class SetRate extends Command {
	
	public static boolean dailyRate = false;
	public static boolean weeklyRate = false;
	public static boolean shopRate = false;
	
	@Override
	public String getName() {
		return "setrate";
	}

	@Override
	public String getDescription() {
		return super.getDescription();
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
		return !super.ownerOnly();
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
		if(args.length != 4) {
			e.getChannel().sendMessage(">setrate day week shop").queue();
			return;
		}
		
		dailyRate = args[1].equals("1") || args[1].equals("true") ? true : false;
		weeklyRate = args[2].equals("1") || args[2].equals("true") ? true : false;
		shopRate = args[3].equals("1") || args[3].equals("true") ? true : false;
	}

}
