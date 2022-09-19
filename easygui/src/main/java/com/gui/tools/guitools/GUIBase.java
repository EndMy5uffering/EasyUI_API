package com.gui.tools.guitools;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.gui.tools.guitools.PermissionGroup.Type;
import com.gui.tools.guitools.annotations.GUIFunction;
import com.gui.tools.guitools.annotations.GUIPermission;
import com.gui.tools.guitools.annotations.MCGUI;
import com.gui.tools.guitools.exceptions.GUIMissingPermission;

public abstract class GUIBase {
	
	private class GUITuple<T,K>{
		public T first;
		public K secound;

		public GUITuple(T t, K k){
			this.first = t;
			this.secound = k;
		}

		public T getFirst(){
			return first;
		}

		public K getSecound(){
			return secound;
		}
	}

	protected String name = "GUI";
	private Map<GUITuple<Integer, ItemStack>, GUIButton> idxItemToButton = new HashMap<>();
	protected Player player;
	protected int size = 9;
	private String tag = "MCGUI";
	protected Inventory inventory;
	private Set<Integer> noEventCancle = new HashSet<>();
	private GUIBase parent;
	private final Object instance;
	private PermissionGroup accessPermissions = new PermissionGroup();
	
	private boolean filterUnaccassable = true;
	
	public GUIBase(Plugin plugin, Player player) {
		this.instance = this;
		this.player = player;

		this.accessPermissions = new PermissionGroup(Type.CONJUNCTIVE);
		for(Annotation a : this.instance.getClass().getAnnotations()) {
			if(a instanceof MCGUI mcgui){
				this.size = mcgui.rows()*9;
				this.name = mcgui.title();
			}
			if(a instanceof GUIPermission perms) accessPermissions.addPermissions(perms.Permissions());
		}

		for(Method m : this.instance.getClass().getMethods()){
			Annotation[] annotations = m.getAnnotations();
			GUIButton button = null;
			PermissionGroup permsGroup = new PermissionGroup(Type.CONJUNCTIVE);
			for(Annotation a : annotations){
				if(a instanceof GUIFunction function){

					ItemStack item = createItem(function.name(), List.of(function.lore()), function.material());
					button = new GUIButton(function.slot(), item, this);
					this.idxItemToButton.put(makeTuple(function.slot(), item), button);

				} else if(a instanceof GUIPermission perms){
					permsGroup.addPermissions(perms.Permissions());
				}
			}
			if(button != null){
				button.setPermissions(permsGroup);
			}
		}
	}
	
	public boolean OpenGUI() {
		inventory = Bukkit.createInventory(this.player, this.size, this.name);
		refreshUI();
		player.openInventory(inventory);
		return true;
	}
	
	public ItemStack createItem(String itemName, List<String> lore, Material m) {
		ItemStack item = new ItemStack(m);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(itemName);
		if(lore != null && lore.size() > 0) meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public void addNoCancleRange(int from, int to) {
		for(int i = from; i <= to; i++) {
			noEventCancle.add(i);
		}
	}
	
	public void addNoCancleRange(int... index) {
		for(int i : index) {
			noEventCancle.add(i);
		}
	}
	
	public void clearNoCancle() {
		if(noEventCancle != null) noEventCancle.clear();
	}

	public void clearUI() {

	}
	
	public void refreshUI() {

	}
	
	public boolean call(EventArgs args) throws GUIMissingPermission{
		GUIButton button = idxItemToButton.get(makeTuple(args.getIndex(), args.getItem()));
		if(button == null) return false;
		return button.call(args);
	}
	
	private GUITuple<Integer, ItemStack> makeTuple(int idx, ItemStack stack){
		return new GUITuple<>(idx, stack);
	}

	public String getTag(){
		return this.tag;
	}

	public int getSize(){
		return this.size;
	}

	public PermissionGroup getAccessPermission(){
		return this.accessPermissions;
	}

	public String getName(){
		return this.name;
	}

	public Player getPlayer(){
		return this.player;
	}
}
