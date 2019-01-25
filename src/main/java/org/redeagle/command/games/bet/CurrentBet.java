package org.redeagle.command.games.bet;

import java.util.ArrayList;
import java.util.Random;

import net.dv8tion.jda.core.JDA;

import org.redeagle.Main;

public class CurrentBet {
	public static ArrayList<Participants> participants = new ArrayList<Participants>();
	public static int bet = 0;
	
	public static void findWinner(JDA bot) {
		Random rnd = new Random();
		if(participants.size() == 0) {
			bot.getGuildById("512994222878490644").getTextChannelById("512999314000314373").sendMessage("Tidak ada pemenang untuk Bet kemaren...").queue();
		}
		else {
			int winnerIdx = rnd.nextInt(participants.size());
			bot.getGuildById("512994222878490644").getTextChannelById("512999314000314373").sendMessage("<@" + participants.get(winnerIdx).id + "> mendapatkan taruhan kemaren sebesar " + (bet*participants.size()) + ", **Congratz!!**").queue();
			int rating = Integer.parseInt(Main.getDB().select("userdata","rating","where discordId='" + participants.get(winnerIdx).id + "'").get(0));
			rating += (bet * participants.size());
			Main.getDB().update("UPDATE userdata SET rating='" + rating + "' where discordId='" + participants.get(winnerIdx).id + "'");
			bot.getUserById(participants.get(winnerIdx).id).openPrivateChannel().queue(ss -> {
				ss.sendMessage("Anda menang bet harian... mendapatkan rating sebesar " + (bet * participants.size()) + " rating").queue(zz -> {
					participants.clear();
				});
			});
			
		}
		bet = rnd.nextInt(3)+1;
		bot.getGuildById("512994222878490644").getTextChannelById("512999314000314373").sendMessage("Hari berganti.. bet hari ini : " + bet).queue();
	}
}
