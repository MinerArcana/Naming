package com.minerarcana.naming.spell;

import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.LanguageMap;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.message.LocalizedMessage;

import javax.annotation.Nonnull;

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

    public abstract boolean cast(@Nonnull Entity caster, String spoken);

    public boolean matches(String spell, String spoken) {
        String contents = LanguageMap.getInstance().getOrDefault(this.getDescriptionId());
        return contents.equalsIgnoreCase(spell);
    }
}
