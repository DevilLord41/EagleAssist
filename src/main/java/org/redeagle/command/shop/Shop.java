package org.redeagle.command.shop;

import java.util.Arrays;
import java.util.List;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;

import org.apache.commons.lang3.math.NumberUtils;
import org.redeagle.Main;
import org.redeagle.command.Command;

public class Shop extends Command {

	@Override
	public String getName() {
		return "shop";
	}

	@Override
	public String getDescription() {
		return "Shop, berbelanja dengan rating yang kamu miliki..";
	}

	@Override
	public List<String> getParameter() {
		return Arrays.asList("<buy | pageNumber | sell>", "<itemId>", "<itemAmount>");
	}

	@Override
	public List<String> getAliases() {
		return super.getAliases();
	}

	@Override
	public boolean ownerOnly() {
		return super.ownerOnly();
	}

	/*
	 * SAMPLE COMMANDS...
	 * IDX
	 * 0 1 2 3 total of 4 length..
	 * >shop buy 0 10 //buying 10 of item with ID 0
	 * >shop 1 //showing shop page 1
	 * >shop sell 0 8 //selling 8 of item with ID 0 
	 */
	
	@Override
	public void onExecute(JDA bot, GuildMessageReceivedEvent e, Message msg, String m, User u, Guild g, String... args) {
		if(args.length == 1) {
			e.getChannel().sendMessage("Harus ada opsi yang tertera, check >help shop").queue();
			return;
		}
		if(args[1].equals("sell")) {
			if(args.length < 3) {
				e.getChannel().sendMessage("Anda harus menyertakan id item yang ingin dibeli").queue();
				return;
			}
			if(args.length < 4) {
				e.getChannel().sendMessage("Anda harus menyertakan jumlah item yang ingin dibeli").queue();
				return;
			}
			if(!NumberUtils.isNumber(args[3])) {
				e.getChannel().sendMessage("Jumlah item hanya bisa diisi dengan angka..").queue();
				return;
			}
			
			int ids = Integer.parseInt(args[2]);
			int amount = Integer.parseInt(args[3]);
			
			int userMoney = Integer.parseInt(Main.getDB().select("userData","rating","where discordID='" + u.getId() + "'").get(0));
			
			for(Item item : Main.getItemHandler().getItemList()) {
				if(item.getId() == ids) {
					if(!item.isSellable()) {
						e.getChannel().sendMessage("Item tersebut tidak bisa dijual.").queue();
						return;
					}
					int currentAmount = ItemUtils.countItem(u, item);
					if(currentAmount < amount) {
						e.getChannel().sendMessage("Anda tidak punya **" + item.getName() + "** sebanyak **" + amount + "** buah").queue();
						return;
					}
					
					ItemUtils.removeItem(u, item, amount);
					userMoney += ((item.getPrice() * 0.25) * amount);
					Main.getDB().update("UPDATE userData SET rating='" + userMoney + "' where discordId='" + u.getId() + "'");
					e.getChannel().sendMessage("Anda telah berhasil menjual **" + (amount) + " " + item.getName() + "** seharga **" + ((item.getPrice() * 0.25) * amount) + "**").queue();
					return;
				}
			}
			e.getChannel().sendMessage("Tidak ada item yang ditemukan dengan ID : " + ids).queue();
			return;
		} else if (args[1].equals("buy")) {
			if(args.length < 3) {
				e.getChannel().sendMessage("Anda harus menyertakan id item yang ingin dijual").queue();
				return;
			}
			if(args.length < 4) {
				e.getChannel().sendMessage("Anda harus menyertakan jumlah item yang ingin dijual").queue();
				return;
			}
			if(!NumberUtils.isNumber(args[3])) {
				e.getChannel().sendMessage("Jumlah item hanya bisa diisi dengan angka..").queue();
				return;
			}
			
			int ids = Integer.parseInt(args[2]);
			int amount = Integer.parseInt(args[3]);
			if(amount <= 0) {
				e.getChannel().sendMessage("Jumlah item harus lebih dari 0").queue();
				return;
			}
			int userMoney = Integer.parseInt(Main.getDB().select("userData","rating","where discordID='" + u.getId() + "'").get(0));
			
			for(Item item : Main.getItemHandler().getItemList()) {
				if(item.getId() == ids) {
					int totalCosts = item.getPrice() * amount;
					if(totalCosts > userMoney) {
						e.getChannel().sendMessage("Sorry, anda tidak memiliki rating yang cukup. Total : " + totalCosts).queue();
						return;
					}
					
					if(item.onlyOne()) {
						if(ItemUtils.countItem(u, item) > 0) {
							e.getChannel().sendMessage("Anda hanya bisa memiliki 1 item ini.").queue();
							return;
						}
					}
					
					if(item.onBuy(bot, e, msg, u, msg.getMember(), amount)) {						
						ItemUtils.addItem(u, item, amount);
						userMoney -= totalCosts;
						Main.getDB().update("UPDATE userData SET rating='" + totalCosts + "' where discordId='" + u.getId() + "'");
						e.getChannel().sendMessage("Anda telah berhasil membeli **" + (amount) + " " + item.getName() + "** seharga **" + totalCosts + "**").queue();
						return;
					} else {
						e.getChannel().sendMessage("Pembelian gagal. Silahkan check deskripsi/id/jumlah item..").queue();
						return;
					}
				}
			}
			
			e.getChannel().sendMessage("Tidak ada item yang ditemukan dengan ID : " + ids).queue();
			return;
		} else if(NumberUtils.isNumber(args[1])) {
			int pageNumber = Integer.parseInt(args[1]);
			if(pageNumber < 1) {
				e.getChannel().sendMessage("Nomor halaman harus 1 / lebih.").queue();
				return;
			}
			if(pageNumber > ((Main.getItemHandler().getItemList().size() / 10) + 1)) {
				e.getChannel().sendMessage("List item tidak ada untuk halaman " + pageNumber).queue();
				return;
			}
		
			EmbedBuilder embedded = new EmbedBuilder();
			embedded.setTitle("Shop Page **" + pageNumber + "** of **" + ((Main.getItemHandler().getItemList().size()/10)+1) + "**");
			for(int i = (pageNumber == 1 ? 0 : ((pageNumber-1) * 10)+1);i < (pageNumber == 1 ? 10 : (pageNumber * 10)+1);i++) {
				if(i > (Main.getItemHandler().getItemList().size()-1)) break;
				embedded.addField(Main.getItemHandler().getItemWithID(i).getName() + "[" + Main.getItemHandler().getItemWithID(i).getId() + "]",  "Price: " + Main.getItemHandler().getItemWithID(i).getPrice() + "\n" + Main.getItemHandler().getItemWithID(i).getDesc(), false);
			}
			e.getChannel().sendMessage(embedded.build()).queue();
		} else {
			e.getChannel().sendMessage("Harus ada opsi yang tertera, check >help shop").queue();
			return;
		}
	}

}