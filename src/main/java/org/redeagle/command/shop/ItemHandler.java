package org.redeagle.command.shop;

import java.util.ArrayList;

public class ItemHandler {
	private ArrayList<Item> itemList = new ArrayList<>();
	
	public void addItem(Item e) {
		itemList.add(e);
	}
	
	public ArrayList<Item> getItemList() {
		return itemList;
	}
	
	public Item getItemWithID(int id) {
		for(Item i : itemList) {
			if(i.getId() == id) {
				return i;
			}
		}
		return null;
	}
	
}
