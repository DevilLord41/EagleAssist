package org.redeagle.event;

import java.util.Calendar;

import net.dv8tion.jda.core.JDA;

import org.redeagle.command.games.bet.CurrentBet;


public class Event {
	public static void dayChangeEvent(JDA bot) {
		int hours = Calendar.HOUR_OF_DAY;
		if(hours == 7) {
			CurrentBet.findWinner(bot);
		}
	}
}
