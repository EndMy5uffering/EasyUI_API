package com.gui.tools.guitools;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;

public class PermissionGroup {
    public enum Type{
        CONJUNCTIVE {
            @Override
            public boolean join(List<Set<String>> perms, Player player) {
                boolean result = true;
                for(Set<String> permSet : perms){
                    boolean subResult = false;
                    for(String perm : permSet){
                        subResult  |= player.hasPermission(perm);
                    }
                    result &= subResult;
                }
                return result;
            }
        },
        DISJUNCTIVE {
            @Override
            public boolean join(List<Set<String>> perms, Player player) {
                boolean result = false;
                for(Set<String> permSet : perms){
                    boolean subResult = true;
                    for(String perm : permSet){
                        subResult  &= player.hasPermission(perm);
                    }
                    result |= subResult;
                }
                return result;
            }
        };

        public abstract boolean join(List<Set<String>> perms, Player player);
    }

    private List<Set<String>> permissions = new ArrayList<>();
    private PermissionGroup.Type type = PermissionGroup.Type.CONJUNCTIVE;

    public PermissionGroup(){

    }

    public PermissionGroup(PermissionGroup.Type type){
        this.type = type;
    }

    public void setPermissions(String[]... perms){
        this.permissions = new ArrayList<>();
        for(String[] parr : perms){
            Set<String> permissionSet = new HashSet<>();
            for(String permission : parr){
                permissionSet.add(permission);
            }
            this.permissions.add(permissionSet);
        }
    }

    public void addPermissions(String[]... perms){
        for(String[] parr : perms){
            Set<String> permissionSet = new HashSet<>();
            for(String permission : parr){
                permissionSet.add(permission);
            }
            this.permissions.add(permissionSet);
        }
    }

    public PermissionGroup addPermissions(String... perms){
        Set<String> permissionSet = new HashSet<>();
        for(String s : perms)
            permissionSet.add(s);
        this.permissions.add(permissionSet);
        return this;
    }

    public boolean hasPermission(Player player){
        return type.join(permissions, player);
    }

}
