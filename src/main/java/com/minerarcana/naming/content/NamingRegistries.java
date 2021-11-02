package com.minerarcana.naming.content;

import com.minerarcana.naming.Naming;
import com.minerarcana.naming.spell.Spell;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("UnstableApiUsage")
public class NamingRegistries {

    public static final Supplier<IForgeRegistry<Spell>> SPELL = Naming.getRegistrate()
            .makeRegistry("spell", Spell.class, RegistryBuilder::new);

    public static void setup() {

    }

    private final static Pattern PATTERN = Pattern.compile("^(?<spell>\\w+)\\s+(?<input>.+)$");

    public static Optional<Pair<Spell, String>> findSpell(String value) {
        Matcher matcher = PATTERN.matcher(value);
        if (matcher.matches()) {
            String spellName = matcher.group("spell");
            String input = matcher.group("input");
            return SPELL.get().getValues()
                    .parallelStream()
                    .filter(spell -> spell.matches(spellName, input))
                    .map(spell -> Pair.of(spell, input))
                    .findFirst();
        }
        return Optional.empty();
    }
}
