package com.gui.tools.guitools;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class EventArgs {

	private final ItemStack item;
	private final int index;
	private final GUIBase gui;
	private final Player player;
	private final InventoryClickEvent event;
	private Object attachedObject;
	
	public EventArgs(ItemStack item, int index, Object attachedObject, GUIBase gui, Player player, InventoryClickEvent e){
		this.item = item;
		this.index = index;
		this.gui = gui;
		this.player = player;
		this.event = e;
		this.attachedObject = attachedObject;
	}

	public ItemStack getItem() {
		return this.item;
	}

	public int getIndex() {
		return this.index;
	}

	public GUIBase getGUI() {
		return this.gui;
	}

	public Player getPlayer() {
		return this.player;
	}

	public InventoryClickEvent getEvent() {
		return this.event;
	}

	public Object getAttachedObject() {
		return this.attachedObject;
	}

	public void setAttachedObject(Object obj){
		this.attachedObject = obj;
	}
	
}
