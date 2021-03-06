package com.minerarcana.naming.spell;

import com.minerarcana.naming.api.capability.INamer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Objects;

public abstract class Spell extends ForgeRegistryEntry<Spell> {
    private String descriptionId;

    public String getDescriptionId() {
        if (descriptionId == null) {
            descriptionId = Util.makeDescriptionId("spell", this.getRegistryName());
        }
        return descriptionId;
    }

    public ITextComponent getDescription() {
        return new TranslationTextComponent(this.getDescriptionId());
    }

    public boolean canCast(@Nonnull Entity caster, @Nonnull INamer namer) {
        return namer.hasAbility(Objects.requireNonNull(this.getRegistryName()).toString());
    }

    public abstract boolean cast(@Nonnull Entity caster, INamer namer, String spoken, Collection<Entity> targeted);

    public int getHoarseTicks() {
        return 100;
    }

    public boolean matches(String spell, String spoken) {
        String contents = LanguageMap.getInstance().getOrDefault(this.getDescriptionId());
        return contents.equalsIgnoreCase(spell);
    }

    public ISpellTargeting getTargeting() {
        return SpellTargeting.AABB_32;
    }
}
