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

public class Unmute extends Command {

	@Override
	public String getName() {
		return "unmute";
	}

	@Override
	public String getDescription() {
		return "unmute user >unmute <mentions> <params>";
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("<mentions>","<-c | -lc | -lcl | -lccl>");
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
		g.getController().addSingleRoleToMember(target, g.getRoleById("512995573637316608")).queue();
		g.getController().addSingleRoleToMember(target, g.getRoleById("512995577521504257")).queue();
		g.getController().removeSingleRoleFromMember(target, g.getRoleById("512995578679001100")).queue();
		g.getController().removeSingleRoleFromMember(target, g.getRoleById("512995578398113792")).queue();
		if(args.length == 3) {
			g.getController().addSingleRoleToMember(target, g.getRoleById("512995253158936598")).queue();
			if(args[2].equals("-c")) {
				g.getController().addSingleRoleToMember(target, g.getRoleById("512995252672659457")).queue();
			} else if(args[2].equals("-lc")) {
				g.getController().addSingleRoleToMember(target, g.getRoleById("513002144111132673")).queue();
			} else if(args[2].equals("-lccl")) {
				g.getController().addSingleRoleToMember(target, g.getRoleById("512995251971948572")).queue();
				g.getController().addSingleRoleToMember(target, g.getRoleById("513002144111132673")).queue();
			}
		}
		e.getChannel().sendMessage(target.getAsMention() + " telah diunmute").queue();
	}
}
