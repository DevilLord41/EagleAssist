package org.redeagle.command.music.utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.managers.AudioManager;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class MusicHandler {
	private final AudioPlayerManager playerManager;
	private final Map<Long, GuildMusicManager> musicManagers;
	public static MusicHandler instance = new MusicHandler();
	private MusicHandler() {
	    this.musicManagers = new HashMap<>();

	    this.playerManager = new DefaultAudioPlayerManager();
	    AudioSourceManagers.registerRemoteSources(playerManager);
	    AudioSourceManagers.registerLocalSource(playerManager);
	}
	
	public static MusicHandler getInstance() {
		return instance;
	}
	
	private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
	    long guildId = Long.parseLong(guild.getId());
	    GuildMusicManager musicManager = musicManagers.get(guildId);
	    
	    if (musicManager == null) {
	      musicManager = new GuildMusicManager(playerManager);
	      musicManagers.put(guildId, musicManager);
	    }

	    guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

	    return musicManager;
	
	}
	
	 public void loadAndPlay(final TextChannel channel, final String trackUrl, final Member m) {
		    GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		    
		    playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
		      @Override
		      public void trackLoaded(AudioTrack track) {
		        channel.sendMessage("Menambahkan **" + track.getInfo().title + "** ke playlist").queue();
		        GuildMusicManager.TrackInfo trackInfo = new GuildMusicManager.TrackInfo();
		        trackInfo.title = track.getInfo().title;
		        trackInfo.length = (track.getInfo().length/1000/60) + "m " + ((track.getInfo().length/1000)%60) + "s";
		        musicManager.queue.add(trackInfo);
		        play(m, musicManager, track);
		      }	

		      @Override
		      public void playlistLoaded(AudioPlaylist playlist) {
		        AudioTrack firstTrack = playlist.getSelectedTrack();

		        if (firstTrack == null) {
		          firstTrack = playlist.getTracks().get(0);
		        }

		        channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();
		        play(m, musicManager, firstTrack);
		      }

		      @Override
		      public void noMatches() {
		        channel.sendMessage("Nothing found by " + trackUrl).queue();
		      }

		      @Override
		      public void loadFailed(FriendlyException exception) {
		        channel.sendMessage("Could not play: " + exception.getMessage()).queue();
		      }
		    });
		  }

		  private void play(Member m, GuildMusicManager musicManager, AudioTrack track) {
		    connectToFirstVoiceChannel(m.getGuild().getAudioManager(), m);

		    musicManager.scheduler.queue(track);
		  }

		  public void skipTrack(TextChannel channel) {
		    GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
		    musicManager.scheduler.nextTrack();

		    channel.sendMessage("Skipped to next track.").queue();
		  }

		  private static void connectToFirstVoiceChannel(AudioManager audioManager, Member m) {
		    if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
		        audioManager.openAudioConnection(m.getVoiceState().getChannel());
		    }
		  }
		  
		  public List<GuildMusicManager.TrackInfo> queue(TextChannel t) {
			  GuildMusicManager g = getGuildAudioPlayer(t.getGuild());
			  
			  return g.queue;
		  }
		  
		  public void stop(TextChannel t) {
			  GuildMusicManager g = getGuildAudioPlayer(t.getGuild());
			  if(t.getGuild().getAudioManager().isConnected()) {
				  queue(t).clear();
				  g.player.stopTrack();
				  t.sendMessage("Music Stopped..").queue();
				  t.getGuild().getAudioManager().closeAudioConnection();
			  } 
		  }
		  
}
