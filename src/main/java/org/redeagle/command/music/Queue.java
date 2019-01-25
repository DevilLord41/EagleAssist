package org.redeagle.command.music;

import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.Command;
import org.redeagle.command.music.utilities.GuildMusicManager;
import org.redeagle.command.music.utilities.MusicHandler;

public class Queue extends Command {

	@Override
	public String getName() {
		return "queue";
	}

	@Override
	public String getDescription() {
		return "Menampilkan playlist/queue";
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
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg,
			String m, User u, Guild g, String... args) {
		MusicHandler music = MusicHandler.getInstance();
		EmbedBuilder builder = new EmbedBuilder();
		for(GuildMusicManager.TrackInfo track : music.queue(msg.getTextChannel())) {
			builder.addField(track.title, "Duration: " + track.length, false);
		}
		msg.getTextChannel().sendMessage(builder.build()).queue();
	}

}
