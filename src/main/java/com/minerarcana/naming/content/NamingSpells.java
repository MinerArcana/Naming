package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.spell.CallingSpell;
import com.minerarcana.naming.spell.HaltingSpell;
import com.minerarcana.naming.spell.Spell;
import com.minerarcana.naming.spell.TamingSpell;
import com.tterrag.registrate.util.entry.RegistryEntry;

public class NamingSpells {
    public static final RegistryEntry<Spell> CALLING = Naming.getRegistrate()
            .object("calling")
            .simple(Spell.class, CallingSpell::new);

    public static final RegistryEntry<Spell> HALTING = Naming.getRegistrate()
            .object("halting")
            .simple(Spell.class, HaltingSpell::new);

    public static final RegistryEntry<Spell> TAMING = Naming.getRegistrate()
            .object("taming")
            .simple(Spell.class, TamingSpell::new);

    public static void setup() {

    }
}
