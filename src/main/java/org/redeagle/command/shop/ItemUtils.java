package org.redeagle.command.shop;

import net.dv8tion.jda.core.entities.User;

import org.redeagle.Main;

public class ItemUtils {
	public static int countItem(User user, Item item) {
		String uid = user.getId();
		if(Main.getDB().select("userInventory","amount","where id='" + uid + "' and discordId='" + item.getId() +"'").size() == 0) {
			return 0;
		}
		return Integer.parseInt(Main.getDB().select("userInventory","amount","where id='" + uid + "' and discordId='" + item.getId() +"'").get(0));
	}
	
	public static boolean removeItem(User user, Item item, int quantity) {
		String uid = user.getId();
		int amount = countItem(user, item);
		if(amount < quantity) return false;
		if(amount == quantity) {
			Main.getDB().delete("userInventory", "where id='" + item.getId() + "' AND discordId='" + uid + "'");
			return true;
		} else if(amount > quantity) {
			Main.getDB().update("UPDATE userInventory SET amount='" + (amount-quantity) + "' where id='" + item.getId() + "' AND discordId='" + uid + "'");
			return true;
		}
		return false;
	}

	public static boolean addItem(User user, Item item, int quantity) {
		String uid = user.getId();
		int amount = countItem(user, item);
		if(amount == 0) {
			Main.getDB().insert("INSERT INTO userInventory VALUES('" + uid + "','" + item.getId() + "','" + quantity + "'");
			return true;
		} else if(amount > 0) {
			Main.getDB().update("UPDATE userInventory SET amount='" + (amount-quantity) + "' where id='" + item.getId() + "' AND discordId='" + uid + "'");
			return true;
		}
		return false;
	}
}
