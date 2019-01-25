package org.redeagle.command.shop.item;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.redeagle.command.shop.Item;

public class Novice extends Item {

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public String getName() {
		return "Novice Title";
	}

	@Override
	public int getPrice() {
		return 999;
	}

	@Override
	public boolean isTradable() {
		return false;
	}
	
	@Override
	public int getAssetValues() {
		return 50;
	}

	@Override
	public String getDesc() {
		return "Mendapatkan 1 Title Novice (roles pada discord), secara permanent.";
	}
	
	@Override
	public String getInfo() {
		return "**" + getName() + "** [" + getId() + "]\n" 
			 + "Sebuah gelar yang membuktikan bahwa pertualangan anda telah dimulai !\n"
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
		return true;
	}

	@Override
	public boolean onItemUsed(JDA bot, GuildMessageReceivedEvent e, Message msg, User u, Member m) {
		if(m.getRoles().contains(e.getGuild().getRoleById("516567307607212042"))) {
			e.getGuild().getController().removeSingleRoleFromMember(m, e.getGuild().getRoleById("516567307607212042")).queue(ss-> {
				e.getChannel().sendMessage("Anda melepas Title **Novice**").queue();
			});
			return true;
		} else if(!m.getRoles().contains(e.getGuild().getRoleById("516567307607212042"))) {
			e.getGuild().getController().removeRolesFromMember(m, m.getRoles()).queue(ss-> {
				e.getGuild().getController().addSingleRoleToMember(m, e.getGuild().getRoleById("516567307607212042")).queue(sz -> {
					e.getChannel().sendMessage("Anda sekarang memakai Title **Novice**").queue();
				});
			});
			return true;
		}
		e.getChannel().sendMessage("Error at Novice.java, ss this at send to <@239627873701330944>").queue();
		return false;
	}
}
