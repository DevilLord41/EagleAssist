package org.redeagle.command.owner;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.apache.commons.lang3.math.NumberUtils;
import org.redeagle.Main;
import org.redeagle.command.Command;

public class AddRating extends Command {

	@Override
	public String getName() {
		return "addrating";
	}

	@Override
	public String getDescription() {
		return "add rating ke user";
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("<mentions>","<rating>");
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
		if(msg.getMentionedMembers().size() == 0) {
			msg.getTextChannel().sendMessage("Please mention 1 member").queue();
			return;
		}
		if(args.length != 3) {
			msg.getTextChannel().sendMessage("Should have rating in number").queue();
			return;
		}
		if(!NumberUtils.isNumber(args[2].replace("-", ""))) {
			msg.getTextChannel().sendMessage("Params 2 must be number").queue();
			return;
		}
		
		Member target = msg.getMentionedMembers().get(0);
		
		long currentRating = Long.parseLong(Main.getDB().select("userdata","rating","WHERE discordId='" + target.getUser().getId() + "'").get(0));
		currentRating += Long.parseLong(args[2]);
		
		Main.getDB().update("UPDATE userdata SET rating='" + currentRating + "' where discordId='" + target.getUser().getId() + "'");
		
		target.getUser().openPrivateChannel().queue((s) -> {
			if(Long.parseLong(args[2]) < 0) {
				s.sendMessage("Rating anda telah dikurangkan sebesar **" + args[2] + "** rating..!").queue();
			} else {
				s.sendMessage("Anda telah mendapatkan Rating sebesar **" + args[2] + "** rating..!").queue();
			}
			
		});
	}
	
}
