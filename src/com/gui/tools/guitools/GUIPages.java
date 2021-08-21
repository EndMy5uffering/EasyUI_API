package com.gui.tools.guitools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class GUIPages extends GUIBase{
	
	private ArrayList<ItemStack> SorceList = new ArrayList<>();
	private Map<Integer, ItemStack> ControleItems = new HashMap<>();
	private Map<Integer, GUIFunction> ControleItemFunctions = new HashMap<>();
	private int page = 0;
	private ItemStack nextPage, prevPage, current, filler;
	
	
	public GUIPages(Player p, int size, String name, String Tag) {
		super(p, (size < 9*2 ? 9 * 2 : size), name, Tag);
		for(int i = 0; i < 9; i++) {
			ControleItems.put(i, filler);
		}
		
	}
	
	@Override
	public boolean OpenGUI() {
		if(!hasAccess()) {
			return false;
		}
		this.inventory = Bukkit.createInventory(this.OpendBy, this.size, this.name);
		
		nextPage = new ItemStack(Material.PAPER);
		ItemMeta nextPageMeta = nextPage.getItemMeta();
		nextPageMeta.setDisplayName("Next Page");
		nextPage.setItemMeta(nextPageMeta);

		prevPage = new ItemStack(Material.PAPER);
		ItemMeta prevPageMeta = prevPage.getItemMeta();
		prevPageMeta.setDisplayName("Previous Page");
		prevPage.setItemMeta(prevPageMeta);

		current = new ItemStack(Material.PAPER);
		ItemMeta currentMeta = current.getItemMeta();
		currentMeta.setDisplayName("Current");
		current.setItemMeta(currentMeta);
		
		filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta fillerMeta = current.getItemMeta();
		fillerMeta.setDisplayName(" ");
		filler.setItemMeta(fillerMeta);
		
		construct();
		
		refreshItems();
		OpendBy.openInventory(inventory);
		return true;
	}
	
	public void addControleItem(int index, ItemStack item) {
		if(index < 9) ControleItems.put(index, item);
	}
	
	public void addControleItemFunctions(int index, GUIFunction f) {
		if(index < 9) ControleItemFunctions.put(index, f);
	}

	int PageCount = 0;
	private void construct() {
		int offset = 0;
		int to = SorceList.size() + (getSize() - 9 - (SorceList.size()%(getSize()-9)));
		for(int i = 0; i < to; i++) {
			if(i % (getSize()-9) == 0 && i > 0) {
				offset += 9;
				PageCount++;
			}
			if(i < SorceList.size()) {
				setItem(i + offset, SorceList.get(i));
			}else {
				setItem(i + offset, filler);
			}
		}
		for(int i = 0; i <= PageCount; i++) {
			setItem((getSize()*i)+(getSize()-9), ControleItems.get(0) != null ? ControleItems.get(0) : filler);
			addGUIFunction((getSize()*i)+(getSize()-9), ControleItemFunctions.get(0), ControleItems.get(0) != null ? ControleItems.get(0).getType() : filler.getType());
			
			setItem((getSize()*i)+(getSize()-8), prevPage);
			addGUIFunction((getSize()*i)+(getSize()-8), x->{
				if(page > 0) {
					page--;
					refreshItems();
				}
			}, Material.PAPER);
			
			setItem((getSize()*i)+(getSize()-7), ControleItems.get(2) != null ? ControleItems.get(2) : filler);
			addGUIFunction((getSize()*i)+(getSize()-7), ControleItemFunctions.get(2), ControleItems.get(2) != null ? ControleItems.get(2).getType() : filler.getType());
			
			setItem((getSize()*i)+(getSize()-6), ControleItems.get(3) != null ? ControleItems.get(3) : filler);
			addGUIFunction((getSize()*i)+(getSize()-6), ControleItemFunctions.get(3), ControleItems.get(3) != null ? ControleItems.get(3).getType() : filler.getType());
			
			current = new ItemStack(Material.PAPER);
			ItemMeta currentMeta = current.getItemMeta();
			int c = i+1;
			int t = PageCount+1;
			currentMeta.setDisplayName(c + "/" + t);
			current.setItemMeta(currentMeta);
			setItem((getSize()*i)+(getSize()-5), current);
			
			setItem((getSize()*i)+(getSize()-4), ControleItems.get(5) != null ? ControleItems.get(5) : filler);
			addGUIFunction((getSize()*i)+(getSize()-4), ControleItemFunctions.get(5), ControleItems.get(5) != null ? ControleItems.get(5).getType() : filler.getType());
			
			setItem((getSize()*i)+(getSize()-3), ControleItems.get(6) != null ? ControleItems.get(6) : filler);
			addGUIFunction((getSize()*i)+(getSize()-3), ControleItemFunctions.get(6), ControleItems.get(6) != null ? ControleItems.get(6).getType() : filler.getType());
			
			setItem((getSize()*i)+(getSize()-2), nextPage);
			addGUIFunction(i*(getSize()-2), x->{
				if(page < PageCount) {
					page++;
					refreshItems();
				}
			}, Material.PAPER);
			
			setItem((getSize()*i)+(getSize()-1), ControleItems.get(8) != null ? ControleItems.get(8) : filler);
			addGUIFunction((getSize()*i)+(getSize()-1), ControleItemFunctions.get(8), ControleItems.get(8) != null ? ControleItems.get(8).getType() : filler.getType());

		}
		
		
	}
	
	@Override
	public void refreshItems() {
		if(this.getInventory() != null) {
			this.getInventory().clear();
			for(int i = 0; i < this.getSize(); i++) {
				this.getInventory().setItem(i, this.getItems().get(i + (this.page * this.getSize())));
			}
		}
	}

	public ItemStack getNextPage() {
		return nextPage;
	}

	public void setNextPage(ItemStack nextPage) {
		this.nextPage = nextPage;
	}

	public ItemStack getCurrent() {
		return current;
	}

	public void setCurrent(ItemStack current) {
		this.current = current;
	}

	public ItemStack getFiller() {
		return filler;
	}

	public void setFiller(ItemStack filler) {
		this.filler = filler;
	}

	public ArrayList<ItemStack> getSorceList() {
		return SorceList;
	}

	public void setSorceList(ArrayList<ItemStack> sorceList) {
		SorceList = sorceList;
	}

	public ItemStack getPrevPage() {
		return prevPage;
	}

	public void setPrevPage(ItemStack prevPage) {
		this.prevPage = prevPage;
	}

}
