package com.gui.tools.guitools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Sets;

public abstract class GUIBase {
	
	protected final String name;
	private Map<Integer, ItemStack> items = new HashMap<>();
	private Map<Integer, Set<String>> itemPerms = new HashMap<>();
	private Map<Integer, Map<Material, GUIFunction>> functions = new HashMap<>();
	private Map<Integer, String> ErrorMessages = new HashMap<>();
	private Map<ItemStack, Object> attachedObjects = new HashMap<>();
	private String defaultErrorMessage = "";
	protected final Player OpendBy;
	protected final int size;
	private final String Tag;
	protected Inventory inventory;
	private Set<String> UIAccessPermissions = new HashSet<String>();
	private Set<Integer> NoEventCancle = new HashSet<>();
	private GUIAccessCondition accessCondition;
	
	private GUIFunction generalFunction;
	
	private boolean filterUnaccassable = true;
	
	public GUIBase(Player p, int size, String name, String Tag) {
		this.name = name;
		this.OpendBy = p;
		this.size = size;
		this.Tag = Tag;
	}
	
	public boolean OpenGUI() {
		if(!hasAccess() && !(accessCondition != null ? accessCondition.access(this) : true)) {
			return false;
		}
		inventory = Bukkit.createInventory(this.OpendBy, this.size, this.name);
		refreshItems();
		OpendBy.openInventory(inventory);
		return true;
	}
	
	public void setItem(int index, ItemStack item) {
		items.put(index, item);
	}

	private int itemCounter = -1;
	public ItemStack getTagedItem(ItemStack item) {
		itemCounter++;
		return setNbtTag(item, "Item:" + itemCounter);
	}
	
	private ItemStack setNbtTag(ItemStack item, String tag) {
		net.minecraft.world.item.ItemStack nbtItem = org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack.asNMSCopy(item);
		net.minecraft.nbt.NBTTagCompound nbtcomp = (nbtItem.s() /*<- was .hasTag()*/) ? nbtItem.t() /*<- was .getTag()*/ : new net.minecraft.nbt.NBTTagCompound();
		nbtcomp.a("CustomGUIID", net.minecraft.nbt.NBTTagString.a(tag)); /*<- was .set(String, NBTBase)*/
		return org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack.asBukkitCopy(nbtItem);
	}
	
	public void attatchObject(int index, Object object) {
		attatchObject(getItem(index), object);
	}
	
	public void attatchObject(ItemStack item, Object object) {
		this.attachedObjects.put(item, object);
	}
	
	public Object getAttachedObject(int index) {
		return getAttatchedObject(getItem(index));
	}
	
	public Object getAttatchedObject(ItemStack item) {
		return this.attachedObjects.get(item);
	}
	
	public void setItem(int index, Material m, String name, List<String> lore) {
		ItemStack item = new ItemStack(m);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		if(lore != null && lore.size() > 0) meta.setLore(lore);
		item.setItemMeta(meta);
		setItem(index, item);
	}
	
	public void addItems(List<ItemStack> items) {
		for(int i = 0; i < items.size(); i++) {
			if(i < this.size) {
				setItem(i, items.get(i));
			}
		}
	}
	
	public void addUIAccessPermission(String... perm) {
		UIAccessPermissions.clear();
		for(String s : perm) {
			UIAccessPermissions.add(s);
		}
	}
	
	public void additemPerms(int index, String... perm) {
		if(!itemPerms.keySet().contains(index)) {
			Set<String> perms = Sets.newHashSet(perm);
			itemPerms.put(index, perms);
		}else {
			Set<String> perms = itemPerms.get(index);
			for(String s : perm) {
				perms.add(s);
			}
			itemPerms.put(index, perms);
		}
	}
	
	public void setGUIAccessCondition(GUIAccessCondition c) {
		this.accessCondition = c;
	}
	
	public ItemStack createItem(String itemName, List<String> lore, Material m) {
		ItemStack item = new ItemStack(m);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(itemName);
		if(lore != null && lore.size() > 0) meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public void addItemErrorMessage(int index, String msg) {
		ErrorMessages.put(index, msg);
	}
	
	public void setDefaultErrorMessage(String msg) {
		this.defaultErrorMessage = msg;
	}
	
	//TODO: this needs restructuring!!!!!
	public void addGUIFunction(int index, GUIFunction f, Material m) {
		if(functions.get(index) != null) {
			Map<Material, GUIFunction> map = functions.get(index);

			//SEE THIS THIS IS NOT GOOD FIX!!!!
			map.put(m,f);
			functions.put(index, map);
		}else {
			Map<Material, GUIFunction> map = new HashMap<>();
			
			//AND THIS SAME PROBLEM
			map.put(m,f);
			functions.put(index, map);
		}
		//PROBLEM: only one function per material!!!!!
	}
	
	public void addNoCancleRange(int from, int to) {
		for(int i = from; i <= to; i++) {
			NoEventCancle.add(i);
		}
	}
	
	public void addNoCancleRange(int... index) {
		for(int i : index) {
			NoEventCancle.add(i);
		}
	}
	
	public void ClearNoCancle() {
		if(NoEventCancle != null) NoEventCancle.clear();
	}
	
	public void ClearUI() {
		if(inventory != null && items != null && itemPerms != null && functions != null) {
			items.clear();
			itemPerms.clear();
			functions.clear();
			inventory.clear();
		}
	}
	
	public void refreshItems() {
		if(inventory != null) {
			inventory.clear();
			items.keySet().stream().filter(x -> {
				if(!itemPerms.keySet().contains(x) || !filterUnaccassable) return true;
				boolean canAccess = false;
				for(String s : itemPerms.get(x)) {
					if(OpendBy.hasPermission(s)) canAccess = true;
				}
				return canAccess;
			}).forEach(x -> inventory.setItem(x, items.get(x)));
		}
	}
	
	public void dispatch(DispatchInformations info){
		boolean hasAccess = false;
		if(NoEventCancle.contains(info.index) && info.event.getClickedInventory().equals(this.inventory)) {
			info.event.setCancelled(false);
		}else {
			info.event.setCancelled(true);
		}
		if(itemPerms.keySet().contains(info.index)) {
			for(String s : itemPerms.get(info.index)) {
				if(OpendBy.hasPermission(s)) {
					hasAccess = true;
				}
			}
			if(!hasAccess) {
				if(ErrorMessages.keySet().contains(info.index)) {
					OpendBy.sendMessage(ErrorMessages.get(info.index));
				}else {
					OpendBy.sendMessage(defaultErrorMessage);
				}
			}
		}else {
			hasAccess = true;
		}
		if(hasAccess && functions.keySet().contains(info.index) && functions.get(info.index).get(info.item.getType()) != null && functions.get(info.index).keySet().contains(info.item.getType())) {
			functions.get(info.index).get(info.item.getType()).dispatch(info);
		}
		
		if(generalFunction != null) {
			generalFunction.dispatch(info);
		}
		
	}
	
	public boolean isActionAllowed(InventoryView inv) {
		if(UIAccessPermissions.size() == 0) return true;
		boolean hasAccess = false;
		if(inv.getTitle().equalsIgnoreCase(name)) {
			for(String s : UIAccessPermissions) {
				if(OpendBy.hasPermission(s)) {
					hasAccess = true;
				}
			}
		}
		return hasAccess;
	}
	
	public boolean hasAccess() {
		if(UIAccessPermissions.size() < 1) return true;
		for(String s : UIAccessPermissions) {
			if(OpendBy.hasPermission(s)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isItemOfInventory(ItemStack item) {
		for(ItemStack i : items.values()) {
			if(item.equals(i)) {
				return true;
			}
		}
		return false;
	}
	
	public ItemStack getItem(int index) {
		if(items.get(index) != null && items.get(index).equals(this.inventory.getItem(index))) {
			return items.get(index);
		}else {
			return null;
		}
	}

	public String getName() {
		return name;
	}

	public Player getPlayer() {
		return OpendBy;
	}

	public String getTag() {
		return Tag;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setGeneralFunction(GUIFunction f) {
		this.generalFunction = f;
	}
	
	public Set<Integer> getNoEventCancle() {
		return NoEventCancle;
	}

	public boolean isFilterUnaccassable() {
		return filterUnaccassable;
	}

	public void setFilterUnaccassable(boolean filterUnaccassable) {
		this.filterUnaccassable = filterUnaccassable;
	}

	public int getSize() {
		return size;
	}

	public Map<Integer, ItemStack> getItems() {
		return items;
	}

	public Map<Integer, Set<String>> getItemPerms() {
		return itemPerms;
	}

	public Map<Integer, Map<Material, GUIFunction>> getFunctions() {
		return functions;
	}

	public Map<Integer, String> getErrorMessages() {
		return ErrorMessages;
	}

	public String getDefaultErrorMessage() {
		return defaultErrorMessage;
	}

	public Set<String> getUIAccessPermissions() {
		return UIAccessPermissions;
	}

	public GUIFunction getGeneralFunction() {
		return generalFunction;
	}

	public Map<ItemStack, Object> getAttachedObjects() {
		return attachedObjects;
	}

	public void setAttachedObjects(Map<ItemStack, Object> attachedObjects) {
		this.attachedObjects = attachedObjects;
	}
	
}
