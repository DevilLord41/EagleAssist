package org.redeagle.command.shop.item;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.shop.Item;
import org.redeagle.command.shop.ItemUtils;

public class Legendary extends Item {

	@Override
	public int getId() {
		return 7;
	}

	@Override
	public String getName() {
		return "Legendary Title";
	}

	@Override
	public String getDesc() {
		return "Mendapatkan 1 Title Legendary (roles discord), secara permanent\n"
				+ "Harus memiliki Title Paladin[6] terlebih dahulu";
	}
	
	@Override
	public String getInfo() {
		return "**" + getName() + "** [" + getId() + "]"
				 + "Anda adalah Legenda !!\n"
				 + "Tidak Bisa Dijual\n"
				 + "Tidak Bisa Ditransfer\n";
	}

	@Override
	public int getPrice() {
		return 9999;
	}

	@Override
	public boolean isSellable() {
		return false;
	}

	@Override
	public boolean isUsable() {
		return true;
	}

	@Override
	public int getAssetValues() {
		return 1500;
	}

	@Override
	public boolean isForgable() {
		return false;
	}

	@Override
	public boolean isEnchantable() {
		return false;
	}

	@Override
	public boolean onlyOne() {
		return true;
	}

	@Override
	public boolean isTradable() {
		return false;
	}

	@Override
	public boolean onBuy(JDA bot, GuildMessageReceivedEvent e, Message msg, User u, Member m, int amount) {
		if(amount > 1) {
			e.getChannel().sendMessage("Hanya bisa membeli 1 item.").queue();
			return false;
		}
		if(ItemUtils.countItem(u, new GrandTemplar()) > 0) {
			e.getChannel().sendMessage("Anda harus memiliki title Paladin terlebih dahulu.").queue();
			return false;
		}
		return true;
	}

	@Override
	public boolean onItemUsed(JDA bot, GuildMessageReceivedEvent e, Message msg, User u, Member m) {
		if(m.getRoles().contains(e.getGuild().getRoleById("516567437806927873"))) {
			e.getGuild().getController().removeSingleRoleFromMember(m, e.getGuild().getRoleById("516567437806927873")).queue(ss-> {
				e.getChannel().sendMessage("Anda melepas Title **Legendary**").queue();
			});
			return true;
		} else if(!m.getRoles().contains(e.getGuild().getRoleById("516567437806927873"))) {
			e.getGuild().getController().removeRolesFromMember(m, m.getRoles()).queue(ss-> {
				e.getGuild().getController().addSingleRoleToMember(m, e.getGuild().getRoleById("516567437806927873")).queue(sz -> {
					e.getChannel().sendMessage("Anda sekarang memakai Title **Legendary**").queue();
				});
			});
			return true;
		}
		e.getChannel().sendMessage("Error at Paladin.java, ss this at send to <@239627873701330944>").queue();
		return false;
	}

}
