package org.redeagle.command.music.utilities;

import java.util.HashMap;

import org.apache.commons.lang3.math.NumberUtils;
import org.redeagle.BotConfiguration;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MusicSelectHandler extends ListenerAdapter {
	public String author = "";
	public HashMap<String,String> musicSelection = new HashMap<String,String>();
	JDA bot = null;
	
	public MusicSelectHandler(JDA bot) {
		this.bot = bot;
	}
	
	public void add(String number, String url) {
		musicSelection.put(number, url);
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		if(event.getAuthor().isBot()) return;
		if(NumberUtils.isNumber(event.getMessage().getContentRaw()) && event.getAuthor().getId().equals(author)) {
			int selection = Integer.parseInt(event.getMessage().getContentRaw());
			if(selection > musicSelection.size()) {
				event.getChannel().sendMessage("Sorry, pilihan tidak tersedia, harap pilih lagi..").queue();
				bot.removeEventListener(this);
				return;
			}
			MusicHandler music = MusicHandler.getInstance();
			music.loadAndPlay(event.getChannel(), musicSelection.get(event.getMessage().getContentRaw()), event.getMember());
			bot.removeEventListener(this);
		}			
	}
	
}
