package com.minerarcana.naming.capability;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.minerarcana.naming.api.capability.INamer;
import com.minerarcana.naming.content.NamingCriteriaTriggers;
import com.minerarcana.naming.content.NamingEffects;
import com.minerarcana.naming.spell.Spell;
import com.minerarcana.naming.util.CachedValue;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Namer implements INamer, INBTSerializable<CompoundTag> {
    public static Capability<INamer> CAP = CapabilityManager.get(new CapabilityToken<>() {
    });

    private final Set<String> abilities;
    private final Set<String> heardHistory;
    private final Set<String> spokenHistory;
    private final Map<Spell, MutableInt> castings;

    private final CachedValue<Integer> totalCastings;

    public Namer() {
        this(Sets.newHashSet(), Sets.newHashSet(), Sets.newHashSet(), Maps.newHashMap());
    }

    public Namer(Set<String> abilities, Set<String> heard, Set<String> spoken, Map<Spell, MutableInt> castings) {
        this.abilities = abilities;
        this.heardHistory = heard;
        this.spokenHistory = spoken;
        this.castings = castings;
        this.totalCastings = new CachedValue<>(() -> this.castings.values()
                .stream()
                .map(MutableInt::intValue)
                .reduce(0, Integer::sum)
        );
    }

    @Override
    @Nonnull
    public Collection<String> getAbilities() {
        return abilities;
    }

    @Override
    public boolean grantAbility(String name) {
        return abilities.add(name);
    }

    @Override
    public boolean removeAbility(String name) {
        return abilities.remove(name);
    }

    @Override
    public boolean hasAbility(String name) {
        return abilities.contains(name);
    }

    @Override
    public void speakTo(String phrase) {
        heardHistory.add(phrase);
    }

    @Override
    public Collection<String> getHeardHistory() {
        return heardHistory;
    }

    @Override
    public void heardBy(String phrase) {
        spokenHistory.add(phrase);
    }

    @Override
    public Collection<String> getSpokenHistory() {
        return spokenHistory;
    }

    @Override
    public void castSpell(Entity caster, Spell spell, String input, Supplier<Collection<Entity>> targeted) {
        boolean cast = this.hasAbility("spells") && spell.canCast(caster, this) && spell.cast(caster, this, input, targeted.get());
        if (cast) {
            this.castings.computeIfAbsent(spell, value -> new MutableInt())
                    .increment();
            this.totalCastings.invalidate();
            if (caster instanceof ServerPlayer) {
                NamingCriteriaTriggers.SPELL.trigger((ServerPlayer) caster);
            }
        }
        if (caster instanceof LivingEntity && spell.getHoarseTicks() > 0) {
            ((LivingEntity) caster).addEffect(new MobEffectInstance(NamingEffects.HOARSE.get(), spell.getHoarseTicks()));
        }
    }

    @Override
    public int getTimesCast(Spell spell) {
        MutableInt spellCastings = castings.get(spell);
        return spellCastings != null ? spellCastings.intValue() : 0;
    }

    @Override
    public int getTotalCastings() {
        return this.totalCastings.get();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.put("abilities", create(abilities));
        nbt.put("heard", create(heardHistory));
        nbt.put("spoken", create(spokenHistory));
        return nbt;
    }

    private ListTag create(Collection<String> list) {
        ListTag listNBT = new ListTag();
        list.stream()
                .map(StringTag::valueOf)
                .forEach(listNBT::add);
        return listNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        abilities.clear();
        addTo(nbt, "abilities", abilities::add);
        heardHistory.clear();
        addTo(nbt, "heard", heardHistory::add);
        spokenHistory.clear();
        addTo(nbt, "spoken", spokenHistory::add);
    }

    private void addTo(CompoundTag nbt, String field, Consumer<String> consumer) {
        ListTag nbtList = nbt.getList(field, Tag.TAG_STRING);
        for (int i = 0; i < nbtList.size(); i++) {
            consumer.accept(nbtList.getString(i));
        }
    }

    public void setAbilities(Collection<String> abilities) {
        this.abilities.clear();
        this.abilities.addAll(abilities);
    }
}
