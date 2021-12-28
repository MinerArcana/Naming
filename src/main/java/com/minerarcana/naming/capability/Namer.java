package com.minerarcana.naming.capability;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.minerarcana.naming.Naming;
import com.minerarcana.naming.api.capability.INamer;
import com.minerarcana.naming.content.NamingCriteriaTriggers;
import com.minerarcana.naming.content.NamingEffects;
import com.minerarcana.naming.spell.Spell;
import com.minerarcana.naming.util.CachedValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Namer implements INamer, INBTSerializable<CompoundNBT> {
    @CapabilityInject(INamer.class)
    public static Capability<INamer> CAP = null;

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
    public Collection<String> getAbilities() {
        return abilities;
    }

    @Override
    public boolean grantAbility(String name) {
        return abilities.add(name);
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
            this.castings.computeIfAbsent(spell, value -> new MutableInt()).increment();
            if (caster instanceof ServerPlayerEntity) {
                NamingCriteriaTriggers.SPELL.trigger((ServerPlayerEntity) caster);
            }
        }
        if (caster instanceof LivingEntity && spell.getHoarseTicks() > 0) {
            ((LivingEntity) caster).addEffect(new EffectInstance(NamingEffects.HOARSE.get(), spell.getHoarseTicks()));
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
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("abilities", create(abilities));
        nbt.put("heard", create(heardHistory));
        nbt.put("spoken", create(spokenHistory));
        return nbt;
    }

    private ListNBT create(Collection<String> list) {
        ListNBT listNBT = new ListNBT();
        list.stream()
                .map(StringNBT::valueOf)
                .forEach(listNBT::add);
        return listNBT;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        abilities.clear();
        addTo(nbt, "abilities", abilities::add);
        heardHistory.clear();
        addTo(nbt, "heard", heardHistory::add);
        spokenHistory.clear();
        addTo(nbt, "spoken", spokenHistory::add);
    }

    private void addTo(CompoundNBT nbt, String field, Consumer<String> consumer) {
        ListNBT nbtList = nbt.getList(field, Constants.NBT.TAG_STRING);
        for (int i = 0; i < nbtList.size(); i++) {
            consumer.accept(nbtList.getString(i));
        }
    }

    public void setAbilities(Collection<String> abilities) {
        this.abilities.clear();
        this.abilities.addAll(abilities);
    }
}
