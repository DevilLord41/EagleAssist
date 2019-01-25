package org.redeagle.command.owner;

import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.BotConfiguration;
import org.redeagle.command.Command;

public class SetStatus extends Command {

	@Override
	public String getName() {
		return "setstatus";
	}

	@Override
	public String getDescription() {
		return "Set Status of the Bot";
	}

	@Override
	public List<String> getParameter() {
		return super.getParameter();
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
		if(args.length == 1) return;
		
		String status = msg.getContentRaw().replace(BotConfiguration.BOT_PREFIX + "setstatus ", "");
		
		bot.getPresence().setGame(Game.listening(status));
	}

}
