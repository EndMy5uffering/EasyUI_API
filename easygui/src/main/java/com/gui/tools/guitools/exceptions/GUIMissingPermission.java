package com.gui.tools.guitools.exceptions;

import org.bukkit.entity.Player;

import com.gui.tools.guitools.GUIBase;

import net.md_5.bungee.api.ChatColor;

public class GUIMissingPermission extends Exception{
    private GUIBase gui;
    private Player player;
    private String message;

    public GUIMissingPermission(Player player, GUIBase gui){
        this.message = ChatColor.RED + "Missing permissions!";
        this.gui = gui;
        this.player = player;
    }
}
