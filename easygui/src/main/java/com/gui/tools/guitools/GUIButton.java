package com.gui.tools.guitools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.inventory.ItemStack;

import com.gui.tools.guitools.PermissionGroup.Type;
import com.gui.tools.guitools.exceptions.GUIMissingPermission;

public class GUIButton {
    
    private ItemStack item;
    private Method method;
    private Object instance;
    private GUIFunction function;
    private int slot;
    private PermissionGroup permissions = new PermissionGroup(Type.CONJUNCTIVE);
    private GUIBase gui;

    public GUIButton(int slot, ItemStack item, GUIBase gui){
        this.slot = slot;
        this.item = item;
        this.gui = gui;
    }

    public void setMethod(Method m, Object instance){
        this.method = m;
        this.instance = instance;
    }

    public void setGUIFunction(GUIFunction f){
        this.function = f;
    }

    public void addPermissions(String... permisisons){
        this.permissions.addPermissions(permisisons);
    }

    public void addPermissions(String[]... permissions){
        this.permissions.addPermissions(permissions);
    }

    public void setPermissions(PermissionGroup permissions){
        this.permissions = permissions;
    }

    public boolean call(EventArgs args) throws GUIMissingPermission{
        if(!permissions.hasPermission(args.getPlayer())) 
            throw new GUIMissingPermission(args.getPlayer(), args.getGUI());

        if(this.method != null && this.instance != null){
            try {
                method.invoke(instance, args);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                return false;
            }
            return true;
        }
        else if(this.function != null) {
            function.call(args);
            return true;
        }
        return false;
    }

}
