package org.redeagle.command.faction;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.Command;

public class Faction extends Command  {

	@Override
	public String getName() {
		return "faction";
	}

	@Override
	public String getDescription() {
		return "Membuat/mengonfigurasikan faction";
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("<--create | --show | --leaderboard | --my>","<factionName | page | page>");
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
		// TODO FACTION LATER... RELEASED IN FEBRUARY.. OR MAYBE MARCH / APR. :D
		// SIBUK UJIAN COK AWKOWAKAW...
		// KLO SELESAI JG GA AKAN W RILIS..
		// PALING W DISABLE DLU
		
	}
 
}
