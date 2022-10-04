package com.minerarcana.naming.api.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface INamer extends INBTSerializable<CompoundTag> {
    @NotNull
    Collection<String> getAbilities();

    boolean grantAbility(String name);

    boolean removeAbility(String name);

    boolean hasAbility(String name);

    /**
     * Used to Speaking Stones, that the Namer can Hear
     *
     * @param phrase The phrase that was spoken
     */
    void speakTo(String phrase);

    /**
     * @return The list of things that were spoken and heard by the Namer (Default Implementation is Uniques only)
     */
    Collection<String> getHeardHistory();

    /**
     * Used by Listening Stones, that hear the Namer speaking
     *
     * @param phrase the phrase that was heard
     */
    void heardBy(String phrase);

    /**
     * @return The list of things that were spoken and heard by Listening Stones (Default Implementation is Uniques Only)
     */
    Collection<String> getSpokenHistory();
}
