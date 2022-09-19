package com.gui.tools.guitools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import com.gui.tools.guitools.exceptions.GUIMissingPermission;


public class InventoryManager implements Listener{

	private static Map<Player, GUIBase> GUIs = new HashMap<Player, GUIBase>();
	
	public static void addGUI(GUIBase gui) {
		GUIs.put(gui.getPlayer(), gui);
	}
	
	public static void removeGUI(GUIBase gui) {
		GUIs.remove(gui.getPlayer());
	}
	
	public static GUIBase getGUIbyPlayer(Player player) {
		return GUIs.get(player);
	}
	
	public static GUIBase getGUIbyInventory(Inventory invent) {
		if(invent.getHolder() instanceof Player player)
			return GUIs.get(player);
		return null;
	}
	
	public static void removeGUIofPlayer(Player p) {
		GUIs.remove(p);
	}
	
	public static Set<GUIBase> getGUIbyTag(String tag) {
		Set<GUIBase> out = new HashSet<>();
		for(GUIBase inv : GUIs.values()) {
			if(inv.getTag().equals(tag)) out.add(inv);
		}
		return out;
	}
	
	@EventHandler
	public void playerInventoryCloseEvent(InventoryCloseEvent e) {
		if(e.getPlayer() instanceof Player) {
			Player p = (Player)e.getPlayer();
			removeGUIofPlayer(p);
		}
	}
	
	@EventHandler
	public void onInventoryInteract(InventoryClickEvent e) {
		Player p = (Player) e.getWhoClicked();
		int rawClick = e.getRawSlot();
		
		if(rawClick < 0) return;
		
		GUIBase gui = getGUIbyPlayer(p);
		if(gui == null) return;
		if(e.getRawSlot() >= e.getInventory().getSize()) {
			InventoryAction action = e.getAction();
	        switch (action) {
	        case MOVE_TO_OTHER_INVENTORY:
	            e.setCancelled(true);
	            return;
	        case HOTBAR_MOVE_AND_READD:
	            e.setCancelled(true);
	            return;
	        case HOTBAR_SWAP:
	            e.setCancelled(true);
	            return;
	        default:
	            break;
	        }
		}
		if(gui.getAccessPermission().hasPermission(p) && e.getView().getTitle().equalsIgnoreCase(gui.getName()) && e.getCurrentItem() == null && e.getRawSlot() < gui.getSize()) {
			
			try {
				gui.call(new EventArgs(e.getCurrentItem(), rawClick, null, gui, p, e));
			} catch (GUIMissingPermission e1) {
				e1.printStackTrace();
			}
		}else if(e.getCurrentItem() != null && e.getRawSlot() < gui.getSize()) {
            e.setCancelled(true);
            
			try {
				gui.call(new EventArgs(e.getCurrentItem(), rawClick, null, gui, p, e));
			} catch (GUIMissingPermission e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	@EventHandler
	public void onDragDrop(InventoryDragEvent e) {
		Player p = (Player) e.getWhoClicked();
		
		GUIBase gui = getGUIbyPlayer(p);
		if(gui == null) return;
		if(gui.getAccessPermission().hasPermission(p) && e.getView().getTitle().equalsIgnoreCase(gui.getName())) {
			for(int i : e.getRawSlots()) {
				if(i < e.getInventory().getSize()) {
					if(e.getCursor() != null && !e.getCursor().equals(e.getOldCursor())) {
						e.setCancelled(true);
					}else if(e.getCursor() == null && e.getOldCursor() != null){
						e.setCancelled(true);
					}
				}
			}	
		}
	}
	
}
