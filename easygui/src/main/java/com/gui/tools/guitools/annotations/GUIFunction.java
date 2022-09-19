package com.gui.tools.guitools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.Material;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface GUIFunction {
    public int slot();
    public Material material();
    public String name() default "";
    public String[] lore() default {};
}
