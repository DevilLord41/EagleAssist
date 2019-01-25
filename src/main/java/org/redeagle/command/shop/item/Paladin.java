package org.redeagle.command.shop.item;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.shop.Item;
import org.redeagle.command.shop.ItemUtils;

public class Paladin extends Item {

	@Override
	public int getId() {
		return 6;
	}

	@Override
	public String getName() {
		return "Paladin Title";
	}

	@Override
	public int getPrice() {
		return 4999;
	}

	@Override
	public String getDesc() {
		return "Mendapatkan 1 Title Paladin (roles discord), secara permanent\n"
				+ "Harus memiliki Title Grand Templar[5] terlebih dahulu";
	}

	@Override
	public String getInfo() {
		return "**" + getName() + "** [" + getId() + "]"
				 + "Pemimpin dari segala pasukan perang, andalah yang terkuat !\n"
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
		return 1000;
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
			e.getChannel().sendMessage("Anda harus memiliki title Grand Templar terlebih dahulu.").queue();
			return false;
		}
		return true;
	}

	@Override
	public boolean onItemUsed(JDA bot, GuildMessageReceivedEvent e, Message msg, User u, Member m) {
		if(m.getRoles().contains(e.getGuild().getRoleById("516567300715970561"))) {
			e.getGuild().getController().removeSingleRoleFromMember(m, e.getGuild().getRoleById("516567300715970561")).queue(ss-> {
				e.getChannel().sendMessage("Anda melepas Title **Paladin**").queue();
			});
			return true;
		} else if(!m.getRoles().contains(e.getGuild().getRoleById("516567300715970561"))) {
			e.getGuild().getController().removeRolesFromMember(m, m.getRoles()).queue(ss-> {
				e.getGuild().getController().addSingleRoleToMember(m, e.getGuild().getRoleById("516567300715970561")).queue(sz -> {
					e.getChannel().sendMessage("Anda sekarang memakai Title **Paladin**").queue();
				});
			});
			return true;
		}
		e.getChannel().sendMessage("Error at Paladin.java, ss this at send to <@239627873701330944>").queue();
		return false;
	}

}
