package org.redeagle.command.moderation;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.Command;

public class SetNickname extends Command {

	@Override
	public String getName() {
		return "setnickname";
	}

	@Override
	public String getDescription() {
		return "Change user nickname (admin). >setnickname <mentions> <nick>";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("changenickname", "nick", "nickname", "cn");
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("mentions","nick");
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg,
			String m, User u, Guild g, String... args) {
		if(!msg.getMember().hasPermission(Permission.NICKNAME_MANAGE)) return;
		if(args.length < 3) return;
		if(msg.getMentionedMembers().size() == 0) return;
		
		Member target = msg.getMentionedMembers().get(0);
		g.getController().setNickname(target, args[2]).queue();		
		e.getChannel().sendMessage("<@" + u.getId() + ">, success changed <@" + target.getUser().getId() + "> nickname to " + args[2]).queue();
		
	}
}
