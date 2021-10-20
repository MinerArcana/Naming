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

    public static ITextComponent NONE = Naming.getRegistrate()
            .addRawLang("button.naming.listening_type.none", "None");

    public static ITextComponent LISTENING = Naming.getRegistrate()
            .addRawLang("button.naming.listening_type.listening", "Listening");

    public static ITextComponent CONSUMING = Naming.getRegistrate()
            .addRawLang("button.naming.listening_type.consuming", "Consuming");

    public static ITextComponent OWNER = Naming.getRegistrate()
            .addRawLang("button.naming.speaking_target.owner", "Owner");

    public static ITextComponent NEARBY = Naming.getRegistrate()
            .addRawLang("button.naming.speaking_target.nearby", "Nearby");

    public static ITextComponent LISTENERS = Naming.getRegistrate()
            .addRawLang("button.naming.speaking_target.listeners", "Listeners");

    public static void setup() {

    }
}
