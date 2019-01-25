package org.redeagle.command.ranked;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.Main;
import org.redeagle.command.Command;
import org.redeagle.debugger.Log;

public class Task extends Command {

	@Override
	public String getName() {
		return "task";
	}

	@Override
	public String getDescription() {
		return "Menampilkan task/misi hari ini.";
	}

	@Override
	public List<String> getParameter() {
		return super.getParameter();
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("showtask","mission");
	}

	@Override
	public boolean ownerOnly() {
		return super.ownerOnly();
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
		ResultSet rs = Main.getDB().select("SELECT * FROM task");

		EmbedBuilder eb = new EmbedBuilder();
		eb.setDescription("**Misi / Task Hari ini**");
		
		try {
			while(rs.next()) {
				String taskID = rs.getString("taskID");
				String taskName = rs.getString("taskName");
				String taskDescription = rs.getString("taskDescription");
				String taskDiff = rs.getString("taskDiff");
				
				eb.addField(taskName + " *[id:" + taskID + "]*",taskDescription + "\nTingkat Kesulitan: " + taskDiff ,false);
			}
			rs.close();
			msg.getTextChannel().sendMessage(eb.build()).queue();
		} catch(SQLException sqle) {
			Log.e(sqle.getLocalizedMessage());
		}
	}
	
}
