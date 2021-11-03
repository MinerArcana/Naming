package com.minerarcana.naming.api.capability;

import com.minerarcana.naming.spell.Spell;
import net.minecraft.entity.Entity;

import java.util.Collection;

public interface INamer {
    Collection<String> getAbilities();

    boolean grantAbility(String name);

    boolean hasAbility(String name);

    /**
     * Used to Speaking Stones, that the Namer can Hear
     * @param phrase The phrase that was spoken
     */
    void speakTo(String phrase);

    /**
     * @return The list of things that were spoken and heard by the Namer (Default Implementation is Uniques only)
     */
    Collection<String> getHeardHistory();

    /**
     * Used by Listening Stones, that hear the Namer speaking
     * @param phrase the phrase that was heard
     */
    void heardBy(String phrase);

    /**
     * @return The list of things that were spoken and heard by Listening Stones (Default Implementation is Uniques Only)
     */
    Collection<String> getSpokenHistory();

    boolean castSpell(Entity caster, Spell spell, String input);

    int getTimesCast(Spell spell);

    int getTotalCastings();
}
