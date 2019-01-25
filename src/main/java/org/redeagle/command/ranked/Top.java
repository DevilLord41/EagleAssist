package org.redeagle.command.ranked;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.apache.commons.lang3.math.NumberUtils;
import org.redeagle.Main;
import org.redeagle.command.Command;
import org.redeagle.debugger.Log;

public class Top extends Command {

	@Override
	public String getName() {
		return "top";
	}

	@Override
	public String getDescription() {
		return "Tampilkan leaderboard.. >top <pages>";
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("<page | null>");
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("lb","leaderboard");
	}

	@Override
	public boolean ownerOnly() {
		return super.ownerOnly();
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
		int page = 0;
		if(args.length == 2) {
			if(NumberUtils.isNumber(args[1])) {
				page = Integer.parseInt(args[1])-1;
				if(page < 0) {
					page = 0;
				}
			} else {
				msg.getTextChannel().sendMessage("Sorry, nomor page hanya bisa berupa angka..").queue();
				return;
			}
		}
		ResultSet rs = Main.getDB().select("SELECT * FROM userdata");
		List<Rank> rank = new ArrayList<Rank>();
		try {
			while(rs.next()) {
				Rank rankmember = new Rank();
				rankmember.id = rs.getString("discordId");
				rankmember.rating = Integer.parseInt(rs.getString("rating"));
				rankmember.matchs = rs.getString("mission");
				
				rank.add(rankmember);
			}
		} catch(SQLException exception) {
			Log.e(exception.getMessage());
 			}
		
		rank.sort(new Comparator<Rank>() {
			@Override
			public int compare(Rank r1, Rank r2) {
				return r2.rating - r1.rating;
			}
			
		});
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription("Top Leaderboard " + (page+1));
		for(int i = (page==0?10*page:(10*page)+1);i < 10*(page+1);i++) {
			boolean can = false;
			for(Member gMember : g.getMembers()) {
				if(gMember.getUser().getId().equals(rank.get(i).id)) {
					can = true;
				}
			}
			if(can) builder.addField((page==0?(i+1):i) + ". " + bot.getUserById(rank.get(i).id).getName(), "Rating: " + rank.get(i).rating + " | Mission : " + rank.get(i).matchs, false);
			if(!can) {
				Main.getDB().delete("userdata","where discordId='" + rank.get(i).id + "'");
			}
		}
		
		msg.getTextChannel().sendMessage(builder.build()).queue();
	}

}

class Rank {
	String id;
	int rating;
	String matchs;
}
