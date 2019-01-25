package org.redeagle.command.core;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.BotConfiguration;
import org.redeagle.command.Command;
import org.redeagle.debugger.Log;
import org.redeagle.utilities.PNGDrawer;

public class Frame extends Command {

	@Override
	public String getName() {
		return "frame";
	}

	@Override
	public String getDescription() {
		return "Menambahkan frame RE ke profile Discordmu";
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
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
		String avatarLink = u.getAvatarUrl()+"?size=512";
		if(getImageSize(avatarLink) == 512) {
			String frame = "https://cdn.discordapp.com/attachments/477480908698419202/496699907449683968/Webp.net-resizeimage.png?size=512";
			PNGDrawer ava = new PNGDrawer();
			ava.init(512,512);
			ava.drawFromURL(avatarLink, 0, 0);
			ava.drawFromURL(frame, 0, 0);
			ava.commit();
			ava.save("pf", u.getId());
			
			e.getChannel().sendFile(new File(BotConfiguration.PATH + "asset/generated/pf_" + u.getId() + ".png"), "pf.png").queue();
		} else {
			String frame = "https://cdn.discordapp.com/attachments/477480908698419202/496699907449683968/Webp.net-resizeimage.png?size="+getImageSize(avatarLink);
			PNGDrawer ava = new PNGDrawer();
			ava.init(getImageSize(avatarLink),getImageSize(avatarLink));
			ava.drawFromURL(avatarLink, 0, 0);
			ava.drawFromURL(frame, 0, 0, getImageSize(avatarLink),getImageSize(avatarLink));
			ava.commit();
			ava.save("pf", u.getId());
			
			e.getChannel().sendFile(new File(BotConfiguration.PATH + "asset/generated/pf_" + u.getId() + ".png"), "pf.png").queue();
		}
		
	}
	
	
	public int getImageSize(String url) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url) .openConnection();
			connection.setRequestProperty(
			    "User-Agent",
			    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_5) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.65 Safari/537.31");
			
			connection.disconnect();
			return ImageIO.read(connection.getInputStream()).getWidth();
		} catch(IOException e) {
			Log.e(e.getMessage());
		}
		return -1;
	}
}
