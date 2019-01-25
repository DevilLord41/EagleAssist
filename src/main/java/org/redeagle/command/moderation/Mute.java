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

public class Mute extends Command {

	@Override
	public String getName() {
		return "mute";
	}

	@Override
	public String getDescription() {
		return "Mute user..";
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("<mentions>");
	}

	@Override
	public List<String> getAliases() {
		return super.getAliases();
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
		if(!msg.getMember().hasPermission(Permission.ADMINISTRATOR)) {
			return;
		}
		
		if(msg.getMentionedMembers().size() == 0) return;
		
		Member target = msg.getMentionedMembers().get(0);
		
		g.getController().removeRolesFromMember(target, target.getRoles()).queue();
		g.getController().addSingleRoleToMember(target, g.getRoleById("512995578679001100")).queue();
		g.getController().addSingleRoleToMember(target, g.getRoleById("512995578398113792")).queue();
		e.getChannel().sendMessage(target.getAsMention() + " telah dimute.").queue();
	}
}