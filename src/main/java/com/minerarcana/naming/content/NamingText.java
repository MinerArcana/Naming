package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import net.minecraft.util.text.ITextComponent;

public class NamingText {
    public static ITextComponent KEY_CATEGORY = Naming.getRegistrate()
            .addRawLang("key.category.name", "Naming");

    public static ITextComponent KEY = Naming.getRegistrate()
            .addRawLang("key.naming.name", "Name");

    public static ITextComponent SCREEN_TITLE = Naming.getRegistrate()
            .addRawLang("screen.naming.title", "Naming");

    public static void setup() {

    }
}
