package com.gui.tools.guitools.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MCGUI {
    public String title() default "GUI";
    public String tag() default "MCGUI";
    public int rows() default 1;
}
