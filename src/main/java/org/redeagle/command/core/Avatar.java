package org.redeagle.command.core;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.Command;

public class Avatar extends Command {

	@Override
	public String getName() {
		return "avatar";
	}

	@Override
	public String getDescription() {
		return "Check your/user avatars, >avatar <mentions>";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("ava");
	}
	
	public List<String> getParameter() {
		return Arrays.asList("mentions,null");
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg,
			String m, User u, Guild g, String... args) {
		if(args.length == 1) {
			e.getChannel().sendMessage(u.getAvatarUrl() + "?size=512").queue();
		} else {
			if(msg.getMentionedMembers().size() == 0) return;
			Member target = msg.getMentionedMembers().get(0);
			
			e.getChannel().sendMessage(target.getUser().getAvatarUrl() + "?size=512").queue();
		}
	}

}
