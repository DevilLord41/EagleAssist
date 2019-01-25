package org.redeagle.command.music;

import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.BotConfiguration;
import org.redeagle.command.Command;
import org.redeagle.command.music.utilities.MusicSelectHandler;

import com.sedmelluq.discord.lavaplayer.format.StandardAudioDataFormats;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.FunctionalResultHandler;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

public class Play extends Command {

	@Override
	public String getName() {
		return "play";
	}

	@Override
	public String getDescription() {
		return super.getDescription();
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
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args)  {
		super.onExecute(bot, e, msg, m, u, g, args);
		AudioPlayerManager manager = new DefaultAudioPlayerManager();
	    AudioSourceManagers.registerRemoteSources(manager);
	    manager.getConfiguration().setOutputFormat(StandardAudioDataFormats.COMMON_PCM_S16_BE);
	    MusicSelectHandler music = new MusicSelectHandler(bot);
	    music.author = u.getId();
	    manager.loadItem("ytsearch: " + (msg.getContentRaw().replace(BotConfiguration.BOT_TOKEN + "play ", "")), new FunctionalResultHandler(null, playlist -> {
	        EmbedBuilder eb = new EmbedBuilder();
	        int ixx = 1;
	        for(AudioTrack track : playlist.getTracks()) {
	        	if(ixx==8)break;
	        	AudioTrackInfo info = track.getInfo();
	        	eb.addField(ixx + ". " + info.title + " [" + info.author + "]", "Duration : " + (info.length/1000/60) + "m " + ((info.length/1000)%60) + "s", false);
	        	music.add(ixx +"", info.uri);
	        	ixx++;
	        }
	        e.getChannel().sendMessage(eb.build()).queue();
	        
	    }, null, null));
	    
	    bot.addEventListener(music);
	    
	}

}
