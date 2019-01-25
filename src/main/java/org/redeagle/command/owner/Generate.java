package org.redeagle.command.owner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.apache.commons.lang3.math.NumberUtils;
import org.redeagle.Main;
import org.redeagle.command.Command;

public class Generate extends Command {

	@Override
	public String getName() {
		return "generate";
	}

	@Override
	public String getDescription() {
		return "Generate daily task";
	}

	@Override
	public List<String> getParameter() {
		return super.getParameter();
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("gen");
	}

	@Override
	public boolean ownerOnly() {
		return super.ownerOnly();
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg,
			String m, User u, Guild g, String... args) {
		if(args.length == 0) return;
		
		if(args[1].equals("-d") || args[1].equals("-w")) {
			if(NumberUtils.isNumber(args[2])) {
				int taskAmount = Integer.parseInt(args[2]);
				Random rnd = new Random();
				ArrayList<Task> taskSet = new ArrayList<Task>();
				if(args[1].equals("-d")) {
					ResultSet set = Main.getDB().select("SELECT * FROM taskList");
					try {
						while(set.next()) {
							Task t = new Task();
							t.taskDesc = set.getString("taskDescription");
							t.taskName = set.getString("taskName");
							t.taskDiff = set.getString("taskDiff");
							taskSet.add(t);
						}
					} catch(SQLException sqle) {
						e.getChannel().sendMessage("An error occured.. (Task Generate)").queue();
					}
				} else if(args[1].equals("-w")) {
					ResultSet set = Main.getDB().select("SELECT * FROM wTaskList");
					try {
						while(set.next()) {
							Task t = new Task();
							t.taskDesc = set.getString("taskDescription");
							t.taskName = set.getString("taskName");
							t.taskDiff = set.getString("taskDiff");
							taskSet.add(t);
						}
					} catch(SQLException sqle) {
						e.getChannel().sendMessage("An error occured.. (wTask Generate)").queue();
					}
				} else {
					e.getChannel().sendMessage("An error occured..").queue();
					return;
				}

				for(int i = 0; i < taskAmount;i++) {
					int selectedTask = rnd.nextInt(taskSet.size()-1);
					if(args[1].equals("-d")) Main.getDB().insert("INSERT INTO task VALUES('00" + i + "','" + taskSet.get(selectedTask).taskName + "','" + taskSet.get(selectedTask).taskDesc + "','" + taskSet.get(selectedTask).taskDiff + "')");
					else if(args[1].equals("-w")) Main.getDB().insert("INSERT INTO wTask VALUES('00" + i + "','" + taskSet.get(selectedTask).taskName + "','" + taskSet.get(selectedTask).taskDesc + "','" + taskSet.get(selectedTask).taskDiff + "')");
					taskSet.remove(selectedTask);
				}
			} else {
				e.getChannel().sendMessage("An error occured..").queue();
			}
		} else {
			e.getChannel().sendMessage("An error occured..").queue();
		}
	}

	static class Task {
		String taskName;
		String taskDesc;
		String taskDiff;
	}
}














