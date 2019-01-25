package org.redeagle.command.games.bet;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.Main;
import org.redeagle.command.Command;

public class Bet extends Command {

	@Override
	public String getName() {
		return "bet";
	}

	@Override
	public String getDescription() {
		return "Bergabung dalam bet harian...";
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("--user","--bet");
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
		if(args.length == 2) {
			System.out.println(args[1]);
			if(args[1].equals("--reset")) {
				if(!msg.getMember().hasPermission(Permission.ADMINISTRATOR)) return;
				CurrentBet.findWinner(bot);
				return;
			} else if(args[1].equals("--user")) {
				String text = "";
				for(Participants p : CurrentBet.participants) {
					text += p.nickname + ",";
				}
				e.getChannel().sendMessage("**Total Participants**: " + CurrentBet.participants.size() + "\n" + text.substring(0, text.length()-1)).queue();
				return;
			} else if(args[1].equals("--current")) {
				e.getChannel().sendMessage("Bet hari ini: " + CurrentBet.bet).queue();
				return;
			} else return;
		}
		for(Participants pp : CurrentBet.participants) {
			if(pp.id.equals(u.getId())) {
				e.getChannel().sendMessage("Anda sudah terdaftar, tunggu besok lagi... !").queue();
				return;
			}
		}
		
		int userCredit = Integer.parseInt(Main.getDB().select("userdata","rating","where discordId='" + u.getId() + "'").get(0));
		if(userCredit < CurrentBet.bet) {
			e.getChannel().sendMessage("Sorry, credit anda tidak cukup.\nBet hari ini : " + CurrentBet.bet).queue();
			return;
		}
		
		Participants p = new Participants();
		p.id = u.getId();
		p.nickname = u.getName();
		CurrentBet.participants.add(p);
		
		userCredit -= CurrentBet.bet;
		Main.getDB().update("UPDATE userdata SET rating='" + userCredit + "' where discordId='" + u.getId() + "'");
		e.getChannel().sendMessage("Anda telah bergabung dalam bet.. good luck !!").queue();
	}
}


