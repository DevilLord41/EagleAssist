package org.redeagle.command.shop.item;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.shop.Item;
import org.redeagle.command.shop.ItemUtils;

public class Knight extends Item {

	@Override
	public int getId() {
		return 3;
	}

	@Override
	public String getName() {
		return "Knight Title";
	}

	@Override
	public int getPrice() {
		return 2799;
	}

	@Override
	public String getDesc() {
		return "Mendapatkan 1 Title Knight (roles discord), secara permanent\n"
				+ "Harus memiliki Title Assassins[2] terlebih dahulu";
	}

	@Override
	public String getInfo() {
		return "**" + getName() + "** [" + getId() + "]"
				 + "Anda telah berbakti pada Kota kelahiran anda, dan mendapatkan gelar ksatria..\n"
				 + "Tidak Bisa Dijual\n"
				 + "Tidak Bisa Ditransfer\n";
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
		return 200;
	}

	@Override
	public boolean isForgable() {
		return false;
	}
	
	@Override
	public boolean isTradable() {
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
	public boolean onBuy(JDA bot, GuildMessageReceivedEvent e, Message msg, User u, Member m, int amount) {
		if(amount > 1) {
			e.getChannel().sendMessage("Hanya bisa membeli 1 item.").queue();
			return false;
		}
		if(ItemUtils.countItem(u, new Assassins()) > 0) {
			e.getChannel().sendMessage("Anda harus memiliki title Rogue terlebih dahulu.").queue();
			return false;
		}
		return true;
	}

	@Override
	public boolean onItemUsed(JDA bot, GuildMessageReceivedEvent e, Message msg, User u, Member m) {
		if(m.getRoles().contains(e.getGuild().getRoleById("516567305698803712"))) {
			e.getGuild().getController().removeSingleRoleFromMember(m, e.getGuild().getRoleById("516567305698803712")).queue(ss-> {
				e.getChannel().sendMessage("Anda melepas Title **Knight**").queue();
			});
			return true;
		} else if(!m.getRoles().contains(e.getGuild().getRoleById("516567305698803712"))) {
			e.getGuild().getController().removeRolesFromMember(m, m.getRoles()).queue(ss-> {
				e.getGuild().getController().addSingleRoleToMember(m, e.getGuild().getRoleById("516567305698803712")).queue(sz -> {
					e.getChannel().sendMessage("Anda sekarang memakai Title **Novice**").queue();
				});
			});
			return true;
		}
		e.getChannel().sendMessage("Error at Knight.java, ss this at send to <@239627873701330944>").queue();
		return false;
	}

}
