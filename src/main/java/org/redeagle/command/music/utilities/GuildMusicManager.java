package org.redeagle.command.music.utilities;

import java.util.ArrayList;
import java.util.List;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildMusicManager {
	  public final AudioPlayer player;
	  public final TrackScheduler scheduler;
	  public List<TrackInfo> queue = new ArrayList<>();
	  public GuildMusicManager(AudioPlayerManager manager) {
	    player = manager.createPlayer();
	    scheduler = new TrackScheduler(player);
	    player.addListener(scheduler);
	  }
	  public AudioPlayerSendHandler getSendHandler() {
	    return new AudioPlayerSendHandler(player);
	  }
	
	  public static class TrackInfo {
			public String title;
			public String length;
	  }
}

