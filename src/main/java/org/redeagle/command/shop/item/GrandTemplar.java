package org.redeagle.command.shop.item;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.shop.Item;
import org.redeagle.command.shop.ItemUtils;

public class GrandTemplar extends Item {

	@Override
	public int getId() {
		return 5;
	}

	@Override
	public String getName() {
		return "Grand Templar Title";
	}

	@Override
	public int getPrice() {
		return 3799;
	}

	@Override
	public String getDesc() {
		return "Mendapatkan 1 Title Grand Templar (roles discord), secara permanent\n"
				+ "Harus memiliki Title Templar[4] terlebih dahulu";
	}

	@Override
	public String getInfo() {
		return "**" + getName() + "** [" + getId() + "]"
				 + "Anda adalah seseorang yang mengabdikan hidup anda di kuil agung!\n"
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
		return 700;
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
		if(ItemUtils.countItem(u, new Templar()) > 0) {
			e.getChannel().sendMessage("Anda harus memiliki title Templar terlebih dahulu.").queue();
			return false;
		}
		return true;
	}

	@Override
	public boolean onItemUsed(JDA bot, GuildMessageReceivedEvent e, Message msg, User u, Member m) {
		if(m.getRoles().contains(e.getGuild().getRoleById("516567561736028171"))) {
			e.getGuild().getController().removeSingleRoleFromMember(m, e.getGuild().getRoleById("516567561736028171")).queue(ss-> {
				e.getChannel().sendMessage("Anda melepas Title **Grand Templar**").queue();
			});
			return true;
		} else if(!m.getRoles().contains(e.getGuild().getRoleById("516567561736028171"))) {
			e.getGuild().getController().removeRolesFromMember(m, m.getRoles()).queue(ss-> {
				e.getGuild().getController().addSingleRoleToMember(m, e.getGuild().getRoleById("516567561736028171")).queue(sz -> {
					e.getChannel().sendMessage("Anda sekarang memakai Title **Grand Templar**").queue();
				});
			});
			return true;
		}
		e.getChannel().sendMessage("Error at GrandTemplar.java, ss this at send to <@239627873701330944>").queue();
		return false;
	}

}
