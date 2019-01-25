package org.redeagle.command.shop;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

public class Item {
	
	public int getId() {
		return -1;
	}
	
	public String getName() {
		return "";
	}
	
	public int getPrice() {
		return -1;
	}
	
	public String getDesc() {
		return "No Description Available";
	}
	
	public String getInfo() {
		return "No information available";
	}
	
	public boolean isSellable() {
		return true;
	}
	
	public boolean isUsable() {
		return false;
	}
	
	public int getAssetValues() {
		return 0;
	}
	
	public boolean isForgable() {
		return false;
	}
	
	public boolean isEnchantable() {
		return false;
	}
	
	public boolean onlyOne() {
		return false;
	}
	
	public boolean isTradable() {
		return true;
	}
	
	public boolean onBuy(JDA bot, GuildMessageReceivedEvent e, Message msg, User u, Member m, int amount) {
		return false;
	}
	
	public boolean onItemUsed(JDA bot, GuildMessageReceivedEvent e, Message msg, User u, Member m) {
		return false;
	}
}
