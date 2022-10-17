package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

@SuppressWarnings("unused")
public class NamingText {
    public static TranslatableComponent KEY_CATEGORY = Naming.getRegistrate()
            .addRawLang("key.category.naming", "Naming");

    public static TranslatableComponent KEY_NAMING = Naming.getRegistrate()
            .addRawLang("key.naming.name", "Name");

    public static TranslatableComponent KEY_RADIAL = Naming.getRegistrate()
            .addRawLang("key.naming.radial", "Open Name Radial");

    public static Component SCREEN_TITLE = Naming.getRegistrate()
            .addRawLang("screen.naming.title", "Naming");

    public static Component NONE = Naming.getRegistrate()
            .addRawLang("button.naming.listening_type.none", "None");

    public static Component LISTENING = Naming.getRegistrate()
            .addRawLang("button.naming.listening_type.listening", "Listening");

    public static Component CONSUMING = Naming.getRegistrate()
            .addRawLang("button.naming.listening_type.consuming", "Consuming");

    public static Component OWNER = Naming.getRegistrate()
            .addRawLang("button.naming.speaking_target.owner", "Owner");

    public static Component NEARBY = Naming.getRegistrate()
            .addRawLang("button.naming.speaking_target.nearby", "Nearby");

    public static Component LISTENERS = Naming.getRegistrate()
            .addRawLang("button.naming.speaking_target.listeners", "Listeners");

    public static Component CURRENT_ABILITIES = Naming.getRegistrate()
            .addRawLang("command.naming.current_abilities", "Current Abilities:");

    public static TranslatableComponent SYNCED = Naming.getRegistrate()
            .addRawLang("command.naming.number_synced", "Synced %s Player(s)");

    public static TranslatableComponent JEI_SPEAK = Naming.getRegistrate()
            .addRawLang("jei.naming.speak", "Speak '%s' to Transmute");

    public static TranslatableComponent MISSING_ABILITY = Naming.getRegistrate()
            .addRawLang("ability.naming.missing", "You lack the ability to do this");

    public static Component CALLING_SPELL = Naming.getRegistrate()
            .addLang("spell", Naming.rl("calling"), "Call");

    public static Component HALTING_SPELL = Naming.getRegistrate()
            .addLang("spell", Naming.rl("halting"), "Halt");

    public static Component TAMING_SPELL = Naming.getRegistrate()
            .addLang("spell", Naming.rl("taming"), "Tame");

    public static Component TAMING_SPELL_ALT = Naming.getRegistrate()
            .addLang("spell", Naming.rl("taming_alt"), "Befriend");

    public static Component ECHO_SPELL = Naming.getRegistrate()
            .addLang("spell", Naming.rl("echo"), "Echo");

    public static void setup() {

    }
}
