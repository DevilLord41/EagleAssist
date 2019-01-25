package org.redeagle.command.music;

import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.Command;
import org.redeagle.command.music.utilities.MusicHandler;

public class Leave extends Command {

	@Override
	public String getName() {
		return "leave";
	}

	@Override
	public String getDescription() {
		return "Mengeluarkan bot dari voice channel";
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
		return super.ownerOnly();
	}

	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
			MusicHandler handler = MusicHandler.getInstance();
			handler.stop(e.getChannel());
			g.getAudioManager().closeAudioConnection();
	}

}
