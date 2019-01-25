package org.redeagle.command.inventory;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.apache.commons.lang3.math.NumberUtils;
import org.redeagle.Main;
import org.redeagle.command.Command;
import org.redeagle.command.shop.Item;
import org.redeagle.command.shop.ItemUtils;

public class Use extends Command {

	@Override
	public String getName() {
		return "use";
	}

	@Override
	public String getDescription() {
		return "Use a consumable item";
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("<itemId>");
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
		if(args.length == 1) {
			e.getChannel().sendMessage("Anda harus menyertakan Item ID").queue();
			return;
		}
		
		if(!NumberUtils.isNumber(args[1])) {
			e.getChannel().sendMessage("Item ID harus berupa angka").queue();
			return;
		}
		
		int itemID = Integer.parseInt(args[1]);
		
		for(Item i : Main.getItemHandler().getItemList()) {
			if(i.getId() == itemID) {
				if(ItemUtils.countItem(u, i) > 0) {
					i.onItemUsed(bot, e, msg, u, e.getMember());
					ItemUtils.removeItem(u, i, 1);
					return;
				} else {
					e.getChannel().sendMessage("Anda tidak memiliki item tersebut").queue();
					return;
				}
			}
		}
		
		e.getChannel().sendMessage("Item ID salah !!").queue();
	}

}
