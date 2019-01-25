package org.redeagle.command.moderation;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.Command;

public class Ban extends Command {

	@Override
	public String getName() {
		return "ban";
	}

	@Override
	public String getDescription() {
		return "Ban user from Discord, >ban <mentions> <reason>";
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("mentions","reason");
	}

	@Override
	public List<String> getAliases() {
		return super.getAliases();
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
		if(!e.getMember().hasPermission(Permission.BAN_MEMBERS)) return;
		if(args.length < 2) {
			e.getChannel().sendMessage("Please Mention Member.").queue();
			return;
		}
		if(args.length < 3) {
			e.getChannel().sendMessage("Please add reason...").queue();
			return;
		}
		String reason = "";
		for(int i = 2; i < args.length;i++) reason += args + " ";
		g.getController().ban(msg.getMentionedMembers().get(0),0,reason).queue();
		e.getChannel().sendMessage(msg.getMentionedMembers().get(0).getUser().getName() + "Berhasil dikick.").queue();
	}
}







