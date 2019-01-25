package org.redeagle.command.shop.item;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.shop.Item;
import org.redeagle.command.shop.ItemUtils;

public class Assassins extends Item {

	@Override
	public String getName() {
		return "Assassins Title";
	}

	@Override
	public int getId() {
		return 2;
	}

	@Override
	public int getPrice() {
		return 2499;
	}

	@Override
	public String getDesc() {
		return "Mendapatkan 1 Title Assassins (roles discord), secara permanent\n"
				+ "Harus mempunyai title Rogue[1] terlebih dahulu";
	}

	@Override
	public boolean isTradable() {
		return false;
	}

	@Override
	public String getInfo() {
		return "**" + getName() + "** [" + getId() + "]"
				 + "Anda telah memasuki dunia yang lebih gelap..\n"
				 + "Tidak Bisa Dijual\n"
				 + "Tidak Bisa Ditransfer\n";
	}

	@Override
	public boolean isSellable() {
		return !super.isSellable();
	}

	@Override
	public boolean isUsable() {
		return true;
	}

	@Override
	public int getAssetValues() {
		return 150;
	}

	@Override
	public boolean isForgable() {
		return super.isForgable();
	}

	@Override
	public boolean isEnchantable() {
		return super.isEnchantable();
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
		if(ItemUtils.countItem(u, new Rogue()) > 0) {
			e.getChannel().sendMessage("Anda harus memiliki title Rogue terlebih dahulu.").queue();
			return false;
		}
		return true;
	}

	@Override
	public boolean onItemUsed(JDA bot, GuildMessageReceivedEvent e, Message msg, User u, Member m) {
		if(m.getRoles().contains(e.getGuild().getRoleById("516567306214572033"))) {
			e.getGuild().getController().removeSingleRoleFromMember(m, e.getGuild().getRoleById("516567306214572033")).queue(ss-> {
				e.getChannel().sendMessage("Anda melepas Title **Assassins**").queue();
			});
			return true;
		} else if(!m.getRoles().contains(e.getGuild().getRoleById("516567306214572033"))) {
			e.getGuild().getController().removeRolesFromMember(m, m.getRoles()).queue(ss-> {
				e.getGuild().getController().addSingleRoleToMember(m, e.getGuild().getRoleById("516567306214572033")).queue(sz -> {
					e.getChannel().sendMessage("Anda sekarang memakai Title **Assassins**").queue();
				});
			});
			return true;
		}
		e.getChannel().sendMessage("Error at Assassins.java, ss this at send to <@239627873701330944>").queue();
		return false;
	}
}
