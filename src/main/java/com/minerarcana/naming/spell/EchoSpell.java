package com.minerarcana.naming.spell;

import com.minerarcana.naming.api.capability.INamer;
import com.minerarcana.naming.content.NamingItemTags;
import com.minerarcana.naming.content.NamingRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Optional;


public class EchoSpell extends Spell {
    private final Ingredient CAN_ECHO = Ingredient.of(NamingItemTags.CAN_ECHO);

    @Override
    public boolean cast(@Nonnull Entity caster, INamer namer, String spoken, Collection<Entity> targeted) {

        Optional<Pair<Spell, String>> spell = NamingRegistries.findSpell(spoken);
        if (spell.isPresent()) {

        } else {

        }

        return true;
    }

    @Override
    public ISpellTargeting getTargeting() {
        return SpellTargeting.NONE;
    }
}
