package org.redeagle.command.ranked;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.Main;
import org.redeagle.command.Command;
import org.redeagle.debugger.Log;

public class Profile extends Command {

	@Override
	public String getName() {
		return "profile";
	}

	@Override
	public String getDescription() {
		return "Check profile sendiri/user >profile <mentions | null>";
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("<mentions | null>");
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
		if(args.length == 1) {
			ResultSet set = Main.getDB().select("SELECT * FROM userdata WHERE discordId='" + u.getId() + "'");
			EmbedBuilder embed = new EmbedBuilder();
			try {
				embed.setDescription("Profile Rank untuk " + bot.getUserById(set.getString("discordId")).getName());
				embed.setThumbnail(bot.getUserById(set.getString("discordId")).getAvatarUrl());
				embed.addField("Nickname",set.getString("nickname").replace("REx", "RE×"),false);
				embed.addField("PUBGM ID", set.getString("id"),false);
				embed.addField("Join to RedEagle", set.getString("joined"),false);
				embed.addField("Rating", set.getString("Rating"),false);
				embed.addField("Total Mission Clear",set.getString("mission"),false);
				msg.getTextChannel().sendMessage(embed.build()).queue();
			} catch (SQLException exception) {
				Log.e(exception.getLocalizedMessage());
			}
			return;
		} else {
			if(msg.getMentionedMembers().size() == 0) {
				msg.getTextChannel().sendMessage("Please, mention 1 user yang mau dicheck profilenya..").queue();
				return;
			}
			Member target = msg.getMentionedMembers().get(0);
			ResultSet set = Main.getDB().select("SELECT * FROM userdata WHERE discordId='" + target.getUser().getId() + "'");
			EmbedBuilder embed = new EmbedBuilder();
			try {
				embed.setDescription("Profile Rank untuk " + bot.getUserById(set.getString("discordId")).getName());
				embed.setThumbnail(bot.getUserById(set.getString("discordId")).getAvatarUrl());
				embed.addField("Nickname",set.getString("nickname").replace("REx", "RE×"),false);
				embed.addField("PUBGM ID", set.getString("id"),false);
				embed.addField("Join to RedEagle", set.getString("joined"),false);
				embed.addField("Rating", set.getString("Rating"),false);
				embed.addField("Total Mission Clear",set.getString("mission"),false);
				msg.getTextChannel().sendMessage(embed.build()).queue();
			} catch (SQLException exception) {
				Log.e(exception.getMessage());
			}
			return;
		}
	}

}
