package com.gui.tools.guitools;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.gui.tools.guitools.annotations.GUIFunction;
import com.gui.tools.guitools.annotations.GUIPermission;
import com.gui.tools.guitools.annotations.MCGUI;

@MCGUI(rows = 5, title = "TestGUI", tag = "TestGUI")
@GUIPermission(Permissions = {"testgui.access"})
public class TestGUI extends GUIBase {

    public TestGUI(Plugin plugin, Player player) {
        super(plugin, player);
    }

    @GUIFunction(slot = 0, name = "testItem", material = Material.STONE, lore = {"test1", "test2"})
    @GUIPermission(Permissions = {"testgui.testfunc"})
    @GUIPermission(Permissions = {"testgui.admin"})
    public void testFunc(EventArgs args){
        if(args.getPlayer() instanceof TestPlayer tp){
            tp.message = "testFunc";
        }
    }
    
}
