package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.spell.CallingSpell;
import com.minerarcana.naming.spell.Spell;
import com.tterrag.registrate.util.entry.RegistryEntry;

public class NamingSpells {
    public static final RegistryEntry<Spell> CALLING = Naming.getRegistrate()
            .object("calling")
            .simple(Spell.class, CallingSpell::new);

    public static void setup() {

    }
}
