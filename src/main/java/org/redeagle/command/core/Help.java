package org.redeagle.command.core;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

public class Help extends Command {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "Command list..";
	}

	@Override
	public List<String> getAliases() {
		return Arrays.asList("?");
	}

	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
		if(args.length > 2) return;
		if(args.length > 1) {
			Main.getCommandHandler().getRegisteredCommand().forEach(cmds -> {
				if(args[1].equals(cmds.getName().replace(">",""))) {
					EmbedBuilder embed = new EmbedBuilder();
					embed.setTitle("Command info for " + cmds.getName());
					embed.setColor(Color.PINK);
					embed.addField("Commands", cmds.getName(), false);
					embed.addField("Description", cmds.getDescription(), false);
					embed.addField("Parameters", cmds.getParameter().toString().replace("[","").replace("]", ""), false);
					embed.addField("Alias", cmds.getAliases().toString().replace("[","").replace("]", ""),false);
					e.getChannel().sendMessage(embed.build()).queue();
				}
			});
			return;
		}
		HashMap<String, List<Command>> category = new HashMap<>();
		Main.getCommandHandler().getRegisteredCommand().forEach(c -> {
			Class<? extends Command> cls = c.getClass();
			String categories = cls.getPackage().getName();
			categories = categories.split("\\.")[categories.split("\\.").length-1];
			
			List<Command> edit = category.get(categories);
			if(edit == null) edit = new ArrayList<>();
			edit.add(c);
			category.put(categories, edit);
		});
		Log.d(category.toString());
		EmbedBuilder embedded = new EmbedBuilder();
		embedded.setTitle("EagleAssists Command list");
		embedded.setDescription("Prefix : >");
		embedded.setColor(Color.PINK);
		embedded.setFooter("use >help <commands>",null);
		category.forEach((cat, cmd) -> {
			StringBuilder line = new StringBuilder();
			for(Command c : cmd) {
				line.append(" `").append(c.getName().replace(">", "")).append("` ");
			}
			embedded.addField(capitalize(cat),line.toString(),false);
			
		});
		e.getChannel().sendMessage(embedded.build()).queue();
	}
	
	public String capitalize(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}